package com.qys.training.biz.ftp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:27 2020/8/4
 */
@Configuration
public class FtpConfig {

    @Value("${qys.file.storage.path}")
    public String filePath;
}
