package com.qys.training.biz.pdf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qys.training.biz.pdf.entity.PDFTextConfig;
import com.qys.training.biz.pdf.entity.PDFTextConfig;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:06 2020/8/6
 */
public interface PDFService {
    void updatePDF(PDFTextConfig config) throws JsonProcessingException;
}
