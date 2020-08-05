package com.qys.training.biz.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:08 2020/8/5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QYSHazelcastClientConfigTest {
    @Autowired
    HazelcastInstance instance;

    @Test
    public void test() {
        final IMap<Integer, String> test = instance.getMap("test");
        test.forEach((k,v) ->System.out.println(k + v));



    }
}