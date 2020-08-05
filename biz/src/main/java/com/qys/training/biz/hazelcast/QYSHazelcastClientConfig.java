package com.qys.training.biz.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Zed, shadowl91@163.com
 * @date 13:08 2020/8/5
 */
@Configuration
public class QYSHazelcastClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(QYSHazelcastClientConfig.class);

    @Value("${qiyuesuo.hazelcast.group.name}")
    private String groupName;

    @Value("${qiyuesuo.hazelcast.cache.expire}")
    public long expire;

    @Value("${qiyuesuo.hazelcast.cache.sys.config.name}")
    public String sysConfigCacheKey;

    @Bean(value = "client")
    public HazelcastInstance hazelcastClient() {
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.logging.type", "slf4j");
        config.getGroupConfig().setName(groupName);
        ClientNetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setAddresses(Arrays.asList("127.0.0.1"))
                .setSmartRouting(true)
                .setRedoOperation(true)
                .setConnectionTimeout(5000)
                .setConnectionAttemptLimit(60);
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        logger.info("hazelcast client started");
        return client;
    }
}
