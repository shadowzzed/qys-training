package com.qys.training.biz.pdf.service.impl;

import clojure.lang.Obj;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.mapper.FtpMapper;
import com.qys.training.biz.pdf.PDFUtils;
import com.qys.training.biz.pdf.entity.PDFTextConfig;
import com.qys.training.biz.pdf.service.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:06 2020/8/6
 */
@Service
public class PDFServiceImpl implements PDFService {

    private static final Logger logger = LoggerFactory.getLogger(PDFServiceImpl.class);

    @Autowired
    FtpMapper ftpMapper;

    @Override
    public void updatePDF(String json) throws JsonProcessingException {
        logger.info("updatePDF param in --> {}", json);
        this.checkJSON(json);
        ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        final String fileOriginalPath = this.getFile(map);
        this.updateFile(fileOriginalPath, map);
    }

    private void checkJSON(String json) {
        if (StringUtils.isEmpty(json))
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
    }

    // 获取文件路径
    private String getFile(Map<String, Object> map) {
        if (!map.containsKey("id")) {
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        }
        final String idString = (String) map.get("id");
        final long id = Long.parseLong(idString);
        final String filePath = ftpMapper.getFilePath(id);
        File file = new File(filePath);
        return filePath;
    }

    // 更新文件
    // 1.同级目录创建一个新的UUID文件
    // 2.写入数据
    // 3.更新数据库 path变为现在的path
    private void updateFile(String path, Map<String, Object> map) {
        final UUID uuid = UUID.randomUUID();
        final PDFTextConfig config = this.getConfig(map);
        try {
            File file = new File(path);
            final String parent_path = file.getParent();
            String newPath = parent_path + "/" + uuid + ".pdf";
            PdfReader reader = new PdfReader(path);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newPath));
            PDFUtils.addText(reader, stamper, config, "C:\\Users\\Administrator\\IdeaProjects\\qys-training\\biz\\src\\main\\resources\\fonts\\");
            file.delete();
            // 更新数据库
            this.updateDB(newPath, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    private float getX(Map<String, Object> map) {
        String absoluteX = "absoluteX";
        if (!map.containsKey(absoluteX))
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        final String xString = (String) map.get(absoluteX);
        return Float.parseFloat(xString);
    }

    private float getY(Map<String, Object> map) {
        String absoluteY = "absoluteY";
        if (!map.containsKey(absoluteY))
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        final String yString = (String) map.get(absoluteY);
        return Float.parseFloat(yString);
    }

    private PDFTextConfig getConfig(Map<String, Object> map) {
        String text = "text";
        String font_style = "font";
        PDFTextConfig config = new PDFTextConfig();
        if (!map.containsKey(text))
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        final String font_style_string = (String) map.get(font_style);
        switch (font_style_string) {
            case "SONG_TI":
                config.setFont(PDFTextConfig.Font.SONG_TI);
                break;
            case "HEI_TI":
                config.setFont(PDFTextConfig.Font.HEI_TI);
                break;
            case "KAI_TI":
                config.setFont(PDFTextConfig.Font.KAI_TI);
                break;
            default:
                break;
        }
//        final PDFTextConfig.Font font = (PDFTextConfig.Font) map.get(font_style);
//        if (font != null)
//            config.setFont(font);
        final float x = this.getX(map);
        final float y = this.getY(map);
        config.setX(x);
        config.setY(y);
        config.setText(((String) map.get(text)));
        config.setPageNum(1);
        config.setFontSize(50);
        config.setBaseColor(BaseColor.BLACK);
        config.setStyle("BOLD");
        config.setAlign("ALIGN_LEFT");
        config.setOrientation("horizontal");
        config.setHeightSpec(20);
        config.setWidthSpec(20);
        return config;
    }

    private com.qys.training.biz.ftp.entity.File updateDB(String path, Map<String, Object> map) {
        File file = new File(path);
        com.qys.training.biz.ftp.entity.File file_db = new com.qys.training.biz.ftp.entity.File();
        try (InputStream inputStream = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[1024];
            int n = 0;
            int count = 0;
            while ((n = inputStream.read(bytes)) != -1) {
                count++;
                md.update(bytes, 0, n);
            }
            final byte[] digest = md.digest();
            final BigInteger bigInteger = new BigInteger(digest);
            final String hash = bigInteger.toString(16);
            file_db.setFileHash(hash);
            long size = (count - 1) << 10 + n;
            file_db.setFileSize(size);
            file_db.setFilePath(path);
            final String idString = (String) map.get("id");
            final long id = Long.parseLong(idString);
            file_db.setFileName(ftpMapper.getFileName(id));
            logger.info("更新入库的file信息：{}",file_db);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
        return file_db;
    }

}
