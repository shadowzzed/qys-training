package com.qys.training.biz.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qys.training.biz.BizTestRunner;
import com.qys.training.biz.auth.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizTestRunner.class)
@EnableAutoConfiguration
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BizTestRunner.class);
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testCreate() {
    	User user = new User();
    	user.setUsername("nick");
    	user.setPassword("111111");
    	Long id = userService.create(user);
    	logger.info("id===>{}",id);
    }

}
