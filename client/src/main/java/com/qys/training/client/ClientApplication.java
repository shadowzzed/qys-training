package com.qys.training.client;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:33 2020/8/5
 */
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApplication.class).web(WebApplicationType.SERVLET).build().run(args);
    }
}
