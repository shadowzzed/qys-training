package com.qys.training.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.pdf.entity.PDFTextConfig;
import com.qys.training.biz.pdf.service.impl.PDFServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:05 2020/8/6
 */
@RestController()
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    PDFServiceImpl pdfService;

    @PostMapping("/update")
    public BaseResult updatePDF(@RequestBody PDFTextConfig config) throws JsonProcessingException {
        pdfService.updatePDF(config);
        return BaseResult.success();
    }
}
