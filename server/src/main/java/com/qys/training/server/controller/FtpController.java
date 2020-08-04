package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.ftp.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:03 2020/8/4
 */
@RestController
@RequestMapping("/ftp")
public class FtpController {
    @Autowired
    private FtpService ftpService;

    @PostMapping(path = "/upload")
    @ResponseBody
    public BaseResult uploadPDF(@RequestParam("file") MultipartFile file) {
        System.out.println(1);
        final int upload = ftpService.upload(file);
        return BaseResult.success(upload);
    }
}
