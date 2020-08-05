package com.qys.training.cache.listener;

import com.hazelcast.core.Client;
import com.hazelcast.core.ClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:54 2020/8/5
 */
public class HazelcastClusterListener implements ClientListener {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastClusterListener.class);

    ApplicationContext applicationContext;

    public HazelcastClusterListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void clientConnected(Client client) {
        logger.info("find client{}", client);
    }

    @Override
    public void clientDisconnected(Client client) {
        logger.info("client{} disconnected", client);
    }
}
