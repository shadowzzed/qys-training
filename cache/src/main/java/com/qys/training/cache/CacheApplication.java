package com.qys.training.cache;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Zed, shadowl91@163.com
 * @date 11:33 2020/8/5
 */
@SpringBootApplication
public class CacheApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheApplication.class).web(WebApplicationType.SERVLET).build().run(args);
    }
}
