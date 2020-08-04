package com.qys.training.biz.ftp.mapper;

import com.qys.training.biz.BizTestRunner;
import com.qys.training.biz.ftp.entity.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:35 2020/8/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizTestRunner.class)
@EnableAutoConfiguration
@Transactional
public class FtpMapperTest {

    @Autowired
    FtpMapper ftpMapper;

    @Test
    public void testInserFile() {
        File file = new File();
        file.setFileHash("1");
        file.setFileName("2");
        file.setFileSize(22L);
        file.setFilePath("22");
        System.out.println(ftpMapper.insertFile(file));
    }
}