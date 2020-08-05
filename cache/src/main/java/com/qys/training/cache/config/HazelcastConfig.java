package com.qys.training.cache.config;

import com.hazelcast.config.*;
import com.hazelcast.core.ClientService;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.qys.training.cache.listener.HazelcastClusterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:03 2020/8/5
 */
@Configuration
public class HazelcastConfig {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);

    @Value("${qiyuesuo.hazelcast.instance.name}")
    private String instanceName;

    @Value("${qiyuesuo.hazelcast.group.name}")
    private String groupName;

    @Value("${qiyuesuo.hazelcast.heart.nohearbeatsec}")
    private String noHeartBeatSec;

    @Value("${qiyuesuo.hazelcast.management.center.enable}")
    private Boolean enableManCenter;

    @Value("${qiyuesuo.hazelcast.management.center.url}")
    private String mancenterUrl;

    @Value("${qiyuesuo.hazelcast.network.port}")
    private int port;

    @Value("${qiyuesuo.hazelcast.members}")
    private String members;

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public HazelcastInstance hazelcastServer() {

        // 基础配置
        Config config = new Config();
        config.setLiteMember(false);
        config.setProperty("hazelcast.logging.type", "slf4j");
        logger.info("server start instanceName--> {}", instanceName);
        config.setInstanceName(instanceName);
        logger.info("server start groupName --> {}", groupName);
        config.getGroupConfig().setName(groupName);

        if (!StringUtils.isEmpty(noHeartBeatSec)) {
            logger.info("server start noHeartBeatSec --> {}", noHeartBeatSec);
            config.setProperty("hazelcast.max.no.heartbeat.seconds", noHeartBeatSec);
        }

        // 配置管理中心
        ManagementCenterConfig managementCenterConfig = config.getManagementCenterConfig();
        logger.info("server start enableManCenter --> {}", enableManCenter);
        managementCenterConfig.setEnabled(enableManCenter);
        if (enableManCenter)
            managementCenterConfig.setUrl(mancenterUrl);

        // 配置网络
        NetworkConfig networkConfig = config.getNetworkConfig();
        logger.info("server start port --> {}", port);
        networkConfig.setPort(port);

        // 默认关闭广播和aws配置
        JoinConfig joinConfig = networkConfig.getJoin();
        MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
        multicastConfig.setEnabled(false);
        AwsConfig awsConfig = joinConfig.getAwsConfig();
        awsConfig.setEnabled(false);

        // TCP
        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(true);
        logger.info("server start members --> {}", members);
        tcpIpConfig.setMembers(Arrays.asList(members.split(",")));

        // instance
        HazelcastInstance server = Hazelcast.newHazelcastInstance(config);
        ClientService clientService = server.getClientService();
        clientService.addClientListener(new HazelcastClusterListener(applicationContext));
        logger.info("hazelcast started");

        return server;
    }
}
