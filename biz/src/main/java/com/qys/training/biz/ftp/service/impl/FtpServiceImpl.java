package com.qys.training.biz.ftp.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.config.FtpConfig;
import com.qys.training.biz.ftp.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
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
    FtpConfig ftpConfig;

    public void upload(MultipartFile multipartFile) {
        if (multipartFile == null)
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());

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
            byte[] bytes = new byte[1024];
            // 记录最后一次读取的结尾序号
            int n = 0;
            // 记录读取次数
            int count = 0;
            while ((n = inputStream.read(bytes)) != 0) {
                count++;
                outputStream.write(bytes, 0, n);
            }
            int size = (count - 1) << 10 + n;
            logger.info("文件总共{} byte", size);

        } catch (IOException e) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    private String getTodayFileDir() {
        final String filePath = ftpConfig.filePath;
        Instant instant = Instant.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String date = formatter.format(instant);
        return filePath + date;
    }
}
