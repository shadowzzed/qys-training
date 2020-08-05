package com.qys.training.biz.ftp.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.config.FtpConfig;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import com.qys.training.biz.ftp.mapper.FtpMapper;
import com.qys.training.biz.ftp.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:22 2020/8/4
 */
@Service
public class FtpServiceImpl implements FtpService {

    private final Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

    @Autowired
    private FtpConfig ftpConfig;

    @Autowired
    private FtpMapper ftpMapper;

    public int upload(MultipartFile multipartFile) {
        // 参数校验
        if (multipartFile == null)
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        String originalFilename = this.checkPDF(multipartFile);
        long fileSize = this.checkFileSize(multipartFile);
        // 开始上传
        com.qys.training.biz.ftp.entity.File file_db = new com.qys.training.biz.ftp.entity.File();
        file_db.setFileSize(fileSize);
        file_db.setFileName(originalFilename);
        final String dirPath = this.getTodayFileDir();
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        final UUID uuid = UUID.randomUUID();
        String filePath = dirPath + "/" + uuid;
        logger.info("创建文件，文件名为{}", filePath);
        File file = new File(filePath);
        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            // 记录最后一次读取的结尾序号
            int n = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, n);
                md.update(bytes, 0, n);
            }
            logger.info("完成文件上传");
            final String md5 = this.getMD5(md);
            file_db.setFilePath(filePath);
            file_db.setFileHash(md5);
            logger.info("插入数据库文件{}", file_db);
            return ftpMapper.insertFile(file_db);
        } catch (IOException | NoSuchAlgorithmException e) {
            // 异常删除文件，保证数据库和文件系统一致性
            file.delete();
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    private String checkPDF(MultipartFile multipartFile) {
        // 判断是否为pdf文本
        final String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(".pdf"))
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        logger.info("upload -- get file name = {}", originalFilename);
        return originalFilename;
    }

    private Long checkFileSize(MultipartFile multipartFile) {
        // 判断是否大于10M
        final long limitSize = this.getLimitSize();
        final long fileSize = multipartFile.getSize();
        logger.info("get limitsize = {}", limitSize);
        if (fileSize > limitSize)
            throw new QysException(BizCodeEnum.FILE_TOO_LARGE.getCode(), BizCodeEnum.FILE_TOO_LARGE.getDescription());
        logger.info("文件总共{} byte", fileSize);
        return fileSize;
    }

    @Override
    public void update(MultipartFile multipartFile, long fileId) {
        this.checkId(fileId);
        // 从数据库中查询出需要更新的文件路径以及文件
        final String filePath = ftpMapper.getFilePath(fileId);
        logger.info("需要更新的文件ID：{}", fileId);
        if (StringUtils.isEmpty(filePath))
            throw new QysException(BizCodeEnum.FILE_NOT_EXISTED.getCode(), BizCodeEnum.FILE_NOT_EXISTED.getDescription());
        // 删除文件
        File file = new File(filePath);
        if (!file.delete())
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        // 新建文件
        com.qys.training.biz.ftp.entity.File file_db = new com.qys.training.biz.ftp.entity.File();
        final String originalFilename = this.checkPDF(multipartFile);
        final long size = this.checkFileSize(multipartFile);
        logger.info("update -- 文件名{},大小{}", originalFilename, size);
        file_db.setFileName(originalFilename);
        file_db.setFileSize(size);
        try (InputStream inputStream = multipartFile.getInputStream();
        OutputStream outputStream = new FileOutputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            // 记录最后一次读取的结尾序号
            int n = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, n);
                md.update(bytes, 0, n);
            }
            logger.info("完成文件上传");
            final String md5 = this.getMD5(md);
            file_db.setFileHash(md5);
            file_db.setId(fileId);
            ftpMapper.updateFile(file_db);
            logger.info("修改的参数{}",file_db);
        } catch (IOException | NoSuchAlgorithmException e) {
            // 失败删除
            file.delete();
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    @Override
    public void download(long id, HttpServletResponse resp) throws UnsupportedEncodingException {
        this.checkId(id);
        //  向数据库查询元数据
        final String path = ftpMapper.getFilePath(id);
        if (StringUtils.isEmpty(path))
            throw new QysException(BizCodeEnum.FILE_NOT_EXISTED.getCode(), BizCodeEnum.FILE_NOT_EXISTED.getDescription());
        final String fileName = ftpMapper.getFileName(id);
        logger.info("下载文件名字{}", fileName);
        // 设置response请求头和ContentType
        resp.setContentType("application/force-download");
        resp.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
        // 写入文件
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = resp.getOutputStream()) {
            byte[] bytes = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, n);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    @Override
    public void delete(long id) {
        // TODO 这里如何保证一致
        this.checkId(id);
        final String filePath = ftpMapper.getFilePath(id);
        if (StringUtils.isEmpty(filePath))
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        File file = new File(filePath);
        if (!file.delete()) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
        ftpMapper.deleteFile(id);
    }

    @Override
    public List<com.qys.training.biz.ftp.entity.File> selectFileDB(QueryFileParam param) {
        // limit语法是第一个参数为起始行，第二个参数为返回行数
        if (param.getPageSize() != 0)
            param.setCurrentPage(param.getPageSize() * param.getCurrentPage());
        return ftpMapper.selectFileDB(param);
    }

    @Override
    public void downloadZIP(List<Long> list, HttpServletResponse resp) throws IOException {
        if (list.size() < 1)
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/x-msdownload");
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("zip.zip", "UTF-8"));
        final List<String> pathList = ftpMapper.selectBatchPath(list);
        this.zipFile(pathList, resp.getOutputStream());
    }

    private String getMD5(MessageDigest md) {
        final byte[] bytes = md.digest();
        final BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }

    private String getTodayFileDir() {
        final String filePath = ftpConfig.filePath;
        ZonedDateTime instant = ZonedDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String date = formatter.format(instant);
        return filePath + date;
    }

    private long getLimitSize() {
        final long size = Integer.parseInt(ftpConfig.limitSize);
        long i = 1 << 30;
        return i * size;
    }

    private void checkId(long fileId) {
        if (fileId < 0)
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
    }

    private String getMd5String(String path) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream inputStream = new FileInputStream(new File(path + ".pdf"))) {
            byte[] bytes = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                md.update(bytes, 0, n);
            }
            final byte[] newBytes = md.digest();
            final BigInteger bigInteger = new BigInteger(1, newBytes);
            System.out.println(bigInteger.toString(16));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void zipFile(List<String> filePath, OutputStream outputStream) throws IOException {
        byte[] bytes = new byte[1024];
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (String path : filePath) {
            logger.info("get path{}",path);
            final File file = new File(path);
            if (!file.exists())
                continue;
            try (InputStream inputStream = new FileInputStream(file)) {
                logger.info("filename {}",file.getName());
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                int n = 0;
                while ((n = inputStream.read(bytes)) != -1) {
                    zipOutputStream.write(bytes, 0, n);
                }
            } catch (IOException e) {
                throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
            }
        }
        zipOutputStream.close();
    }
}
