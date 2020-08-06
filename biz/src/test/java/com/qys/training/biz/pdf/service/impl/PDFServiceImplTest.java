package com.qys.training.biz.pdf.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:11 2020/8/6
 */
public class PDFServiceImplTest {
    @Test
    public void test() throws JsonProcessingException {
        String json = "{\n" +
                "    \"cacheKey\": \"test\",\n" +
                "    \"key\": \"test1436\",\n" +
                "    \"value\": \"test1436\",\n" +
                "    \"fileName\": \"filename\"\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map.get("cacheKey"));
        map.forEach((k, v) -> System.out.println(k + '\t' + v));

        // 不能转
//        final File file = mapper.readValue(json, File.class);
//        System.out.println(file);
    }

    @Test
    public void test1() {
        File file = new File("C:\\Users\\Administrator\\IdeaProjects\\qys-traning\\qys-traning\\server\\target\\config\\application.properties");
        System.out.println(file.getParent());
    }

    @Test
    public void pathTest() throws IOException, DocumentException {
        System.out.println(BaseFont.createFont("src\\main\\resources\\fonts\\simhei.ttf", "utf-8", false, false));
    }

}