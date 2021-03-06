package com.qys.training.biz.ftp.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.config.FtpConfig;
import com.qys.training.biz.ftp.entity.FtpFile;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import com.qys.training.biz.ftp.mapper.FtpMapper;
import com.qys.training.biz.ftp.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public long upload(MultipartFile multipartFile) {
        // 参数校验
        if (multipartFile == null)
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        // 创建今日文件夹
        final String dirPath = this.getTodayFileDir();
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        // 写入
        return this.writeToSys(multipartFile, dirPath, -1);
    }

    /**
     * 判断是否是pdf
     * @param multipartFile
     * @return 原始文件名
     */
    private String checkPDF(MultipartFile multipartFile) {
        // 判断是否为pdf文本
        final String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename) || !originalFilename.endsWith(".pdf"))
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        logger.info("upload -- get file name = {}", originalFilename);
        return originalFilename;
    }

    /**
     * 判断是否大于10MB
     * @param multipartFile
     * @return 文件大小
     */
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

    /**
     * 写入文件系统
     * @param multipartFile
     * @param path
     * @return 哈希值
     */
    private String writeToFileSysAndGetMd5(MultipartFile multipartFile, String path) {
        File file = new File(path);
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
            return this.getMD5(md);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    /**
     * 写入系统
     * 分为两个部分 写入数据库，写入文件系统
     * @param multipartFile
     * @param id
     * @return 表中的id
     */
    private long writeToSys(MultipartFile multipartFile, String basePath, long id) {
        final String originalFilename = this.checkPDF(multipartFile);
        final Long fileSize = this.checkFileSize(multipartFile);
        FtpFile file_db = new FtpFile();
        file_db.setFileSize(fileSize);
        file_db.setFileName(originalFilename);
        final UUID uuid = UUID.randomUUID();
        String filePath = basePath + "/" + uuid.toString();
        // 写入文件系统
        final String md5 = this.writeToFileSysAndGetMd5(multipartFile, filePath);
        file_db.setFileHash(md5);
        file_db.setFilePath(filePath);
        if (id > 0) {
            logger.info("插入数据{}", file_db);
            ftpMapper.insertFile(file_db);
        }
        else {
            logger.info("更新数据{}", file_db);
            file_db.setId(id);
            ftpMapper.updateFile(file_db);
        }
        return file_db.getId();
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
        this.writeToSys(multipartFile, file.getParent(), fileId);
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
    public List<FtpFile> selectFileDB(QueryFileParam param) {
        // limit语法是第一个参数为起始行，第二个参数为返回行数
        if (param.getPageSize() != 0)
            param.setCurrentPage(param.getPageSize() * param.getCurrentPage());
        if (StringUtils.isEmpty(param.getFileName()))
            param.setFileName("%" + param.getFileName() + "%");
        if (param.getStartSize() > param.getEndSize() && param.getEndSize() != 0)
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        return ftpMapper.selectFileDB(param);
    }

    @Override
    public void downloadZIP(List<Long> list, HttpServletResponse resp) throws IOException {
        if (list.size() < 1)
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/x-msdownload");
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(this.getDate() + ".zip", "UTF-8"));
        final List<FtpFile> pathList = ftpMapper.selectBatchPath(list);
        this.zipFile(pathList, resp.getOutputStream());
    }

    private String getMD5(MessageDigest md) {
        final byte[] bytes = md.digest();
        final BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }

    private String getTodayFileDir() {
        final String filePath = ftpConfig.filePath;
        return filePath + this.getDate();
    }

    private String getDate() {
        ZonedDateTime instant = ZonedDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(instant);
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

    private void zipFile(List<FtpFile> filePath, OutputStream outputStream) throws IOException {
        byte[] bytes = new byte[1024];
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        int count = 0;
        for (FtpFile file_db : filePath) {
            logger.info("get path{}",file_db.getFilePath());
            final File file = new File(file_db.getFilePath());
            if (!file.exists())
                continue;
            try (InputStream inputStream = new FileInputStream(file)) {
                logger.info("filename {}",file_db.getFileName());
                zipOutputStream.putNextEntry(new ZipEntry(++count + "." + file_db.getFileName()));
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
