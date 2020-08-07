package com.qys.training.biz.pdf.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.ftp.entity.FtpFile;
import com.qys.training.biz.ftp.mapper.FtpMapper;
import com.qys.training.biz.pdf.PDFUtils;
import com.qys.training.biz.pdf.config.PDFServiceConfig;
import com.qys.training.biz.pdf.entity.PDFTextConfig;
import com.qys.training.biz.pdf.service.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @Autowired
    PDFServiceConfig pdfServiceConfig;

    @Override
    public void updatePDF(PDFTextConfig config) throws JsonProcessingException {
        logger.info("updatePDF param in --> {}", config);
        this.updateFile(config);
    }

    // 获取文件路径
    private String getFilePath(PDFTextConfig config) {
        final Long id = config.getId();
        if (id == null)
            throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
        return ftpMapper.getFilePath(id);
    }

    // 更新文件
    // 1.同级目录创建一个新的UUID文件
    // 2.写入数据
    // 3.更新数据库
    private void updateFile(PDFTextConfig config) {
        final UUID uuid = UUID.randomUUID();
        final String path = this.getFilePath(config);
        try {
            File file = new File(path);
            final String parent_path = file.getParent();
            String newPath = parent_path + "/" + uuid + ".pdf";
            PdfReader reader = new PdfReader(path);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newPath));
            PDFUtils.addText(reader, stamper, config, pdfServiceConfig.fontPathBase);
            file.delete();
            // 更新数据库
            this.updateDB(newPath, config.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
    }

    private FtpFile updateDB(String path, Long id) {
        File file = new File(path);
        FtpFile file_db = new FtpFile();
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
            file_db.setFileName(ftpMapper.getFileName(id));
            logger.info("更新入库的file信息：{}",file_db);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new QysException(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription());
        }
        return file_db;
    }

}
