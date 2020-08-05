package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.base.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 16:17 2020/8/5
 */
@RestController
@RequestMapping("heart")
public class HeartBeatCheckController {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/check")
    public BaseResult checkHeart() {
        try {
            restTemplate.getForEntity("http://127.0.0.1:8888", Object.class);
        } catch (Exception e) {
            return BaseResult.success("offline");
        }
        return BaseResult.success("online");
    }

}
