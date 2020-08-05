package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.ftp.entity.File;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import com.qys.training.biz.ftp.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:03 2020/8/4
 */
@RestController
@RequestMapping("/ftp")
public class FtpController {
    @Autowired
    private FtpService ftpService;

    private Logger logger = LoggerFactory.getLogger(FtpController.class);

    @PostMapping(path = "/upload")
    @ResponseBody
    public BaseResult uploadPDF(@RequestParam("file") MultipartFile file) {
        final long upload = ftpService.upload(file);
        return BaseResult.success(upload);
    }

    @PostMapping(path = "/update")
    @ResponseBody
    public BaseResult updatePDF(@RequestParam("file") MultipartFile file, @RequestParam("id")long id) {
        ftpService.update(file, id);
        return BaseResult.success();
    }

    @GetMapping(path = "/download")
    @ResponseBody
    public BaseResult downloadPDF(@RequestParam("id")long id, HttpServletResponse resp) throws UnsupportedEncodingException {
        ftpService.download(id, resp);
        return null;
    }

    @DeleteMapping(path = "/delete")
    @ResponseBody
    public BaseResult deletePDF(@RequestParam("id") long id) {
        ftpService.delete(id);
        return BaseResult.success();
    }

    @GetMapping(path = "/selectbatch")
    @ResponseBody
    public BaseResult selectBatch(@RequestBody QueryFileParam param) {
        final List<File> files = ftpService.selectFileDB(param);
        return BaseResult.success(files);
    }

    @GetMapping(path = "/downloadzip")
    @ResponseBody
    public BaseResult downloadZip(HttpServletResponse resp, @RequestBody List<Long> list) throws IOException {
        logger.info("param in{}",list);
        ftpService.downloadZIP(list, resp);
        return null;
    }
}
