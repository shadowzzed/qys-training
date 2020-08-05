package com.qys.training.biz.ftp.mapper;

import com.qys.training.biz.BizTestRunner;
import com.qys.training.biz.ftp.entity.File;
import com.qys.training.biz.ftp.entity.QueryFileParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void testInsertFile() {
        File file = new File();
        file.setFileHash("6e3f1fe8b4dfe696ebef9df6abbe0a13");
        file.setFileName("契约锁-Linux应用扩容指南.pdf");
        file.setFileSize(83347L);
        file.setFilePath("base/files/2020-08-04/537fd20e-29cf-41f5-97a7-12a5e72f122b");
        System.out.println(ftpMapper.insertFile(file));
    }

    @Test
    public void testFindFilePath() {
        System.out.println(ftpMapper.getFilePath(5));
    }

    @Test
    public void updateTest() {
        File file = new File();
        file.setFileHash("1");
        file.setFileSize(1L);
        file.setFileName("22");
        file.setId(2L);
        ftpMapper.updateFile(file);
    }

    @Test
    public void selectFileDBTest() {
        QueryFileParam p = new QueryFileParam();
//        p.setEndSize(1000000000L);
//        p.setStartSize(1L);
        p.setCurrentPage(1 * 5);
        p.setPageSize(5);
        final List<File> list = ftpMapper.selectFileDB(p);
        list.forEach(System.out::println);
        System.out.println(list.size());
    }

    @Test
    public void selectBatchPathTest() {
        List<Long> list = new ArrayList<>(Arrays.asList(2L, 5L));
        ftpMapper.selectBatchPath(list).forEach(System.out::println);
    }
}