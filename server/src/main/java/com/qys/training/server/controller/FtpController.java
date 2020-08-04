package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import com.qys.training.biz.ftp.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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
        final int upload = ftpService.upload(file);
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
        return BaseResult.success();
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
        ftpService.selectFileDB(param);
        return BaseResult.success();
    }
}
