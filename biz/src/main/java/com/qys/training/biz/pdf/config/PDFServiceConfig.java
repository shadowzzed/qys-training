package com.qys.training.biz.pdf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:02 2020/8/7
 */
@Configuration
public class PDFServiceConfig {
    @Value("${qys.file.pdf.font.path}")
    public String fontPathBase;

}
