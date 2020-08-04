package com.qys.training.biz.ftp.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:22 2020/8/4
 */
public interface FtpService {
    void upload(MultipartFile file);
}
