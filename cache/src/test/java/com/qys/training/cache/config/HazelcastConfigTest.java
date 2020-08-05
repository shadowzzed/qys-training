package com.qys.training.cache.config;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zed, shadowl91@163.com
 * @date 12:32 2020/8/5
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HazelcastConfigTest {
    @Value("${test.value}")
    private Boolean value;
    @Test
    public void testValue() {
        System.out.println(value.getClass());
    }

}