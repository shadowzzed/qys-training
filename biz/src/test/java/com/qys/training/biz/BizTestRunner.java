package com.qys.training.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.qys.training")
@MapperScan("com.qys.training.biz.*.mapper")
public class BizTestRunner {

    private static final Logger logger = LoggerFactory.getLogger(BizTestRunner.class);

    public static void main(String[] args){
        SpringApplication.run(BizTestRunner.class,args);
    }

}
