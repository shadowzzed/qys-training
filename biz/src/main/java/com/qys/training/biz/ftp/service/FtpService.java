package com.qys.training.biz.ftp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:22 2020/8/4
 */
public interface FtpService {
    int upload(MultipartFile file);
    void update(MultipartFile file, long fileId);
    void download(long id, HttpServletResponse resp) throws UnsupportedEncodingException;
    void delete(long id);
}
