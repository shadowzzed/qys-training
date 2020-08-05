package com.qys.training.biz.hazelcast.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:41 2020/8/5
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheServiceImplTest {

    @Autowired
    HazelcastInstance instance;

    @Test
    void insertCacheMap() {
        final IMap<Object, Object> test = instance.getMap("test");
        test.forEach((k, v) -> System.out.println(k + "   " + v));
    }
}