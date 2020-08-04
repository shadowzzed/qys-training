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
    public void testInserFile() {
        File file = new File();
        file.setFileHash("8430021a539b7e0d7ee2cd3297b6dbe");
        file.setFileName("劳动合同参数模版-亘岩网络-lx-签署日志.pdf");
        file.setFileSize(288048L);
        file.setFilePath("base/files/2020-08-04/e44e2e99-c811-4843-b7a9-d108341081d5");
        System.out.println(ftpMapper.insertFile(file));
    }

    @Test
    public void testFindFilePath() {
        System.out.println(ftpMapper.getFilePath(2));
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
        p.setStartSize(1L);
        final List<File> list = ftpMapper.selectFileDB(p);
        list.forEach(System.out::println);
        System.out.println(list.size());
    }
}