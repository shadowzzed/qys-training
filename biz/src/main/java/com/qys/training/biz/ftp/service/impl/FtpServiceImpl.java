package com.qys.training.biz.ftp.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.config.FtpConfig;
import com.qys.training.biz.ftp.mapper.FtpMapper;
import com.qys.training.biz.ftp.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
        if (multipartFile == null)
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        final String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(".pdf"))
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        logger.info("get file name = {}", originalFilename);
        final long limitSize = this.getLimitSize();
        final long fileSize = multipartFile.getSize();
        logger.info("get limitsize = {}", limitSize);
        if (fileSize > limitSize)
            throw new QysException(BizCodeEnum.FILE_TOO_LARGE.getCode(), BizCodeEnum.FILE_TOO_LARGE.getDescription());
        logger.info("文件总共{} byte", fileSize);
        com.qys.training.biz.ftp.entity.File file_db = new com.qys.training.biz.ftp.entity.File();
        file_db.setFileSize(fileSize);
        file_db.setFileName(multipartFile.getName());
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
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
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
}
