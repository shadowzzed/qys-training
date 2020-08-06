package com.qys.training.open;

import com.qys.training.biz.heartbeat.HeartBeatRunnable;
import com.qys.training.biz.heartbeat.RestUtils;
import com.qys.training.open.heartbeat.HearBeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:49 2020/8/5
 */
@SpringBootApplication
public class OpenapiApplication {

    @Value("${qiyuesuo.heart.beat.server}")
    private String host;

    @Value("${qiyuesuo.heart.beat.myport}")
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(OpenapiApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(OpenapiApplication.class).web(WebApplicationType.SERVLET).build().run(args);
    }


    @Bean
    public HearBeat hearBeat() {
        RestUtils.sendGet(host, port);
        new Thread(new HeartBeatRunnable(port, false)).start();
        return null;
    }



}
