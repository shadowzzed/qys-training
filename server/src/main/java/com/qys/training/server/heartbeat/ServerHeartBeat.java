package com.qys.training.server.heartbeat;

import com.qys.training.biz.heartbeat.HeartBeatRunnable;
import com.qys.training.biz.heartbeat.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @author Zed, shadowl91@163.com
 * @date 17:14 2020/8/5
 */
@Component
public class ServerHeartBeat {

    private static final Logger logger = LoggerFactory.getLogger(ServerHeartBeat.class);

    @Value("${qiyuesuo.heart.beat.server.port}")
    private int port;
    @Bean
    public ServerHeartBeat init() {
        new Thread(new HeartBeatRunnable(port, true)).start();
        return null;
    }

    @Scheduled(cron = "0/15 * * * * ? ")
    public void sendHeartBeat() {
        final List<String> list = HeartBeatRunnable.getAddList();
//        list.forEach(logger::info);
        logger.info("开始检测客户端心跳，目前list.size = {}", list.size());
        list.removeIf(host -> !RestUtils.sendGet(host, port));
        logger.info("存活列表--------");
        list.forEach(logger::info);
    }
}
