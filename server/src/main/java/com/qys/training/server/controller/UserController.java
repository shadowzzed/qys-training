package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.auth.entity.User;
import com.qys.training.biz.auth.service.UserService;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(path = "/create")
    public @ResponseBody
    BaseResult create(@RequestBody User user){
        Long id = this.userService.create(user);
        return BaseResult.success(id);
    }

    @GetMapping(path = "/check")
    @ResponseBody
    public BaseResult checkUser(@RequestBody User user) {
        final User user_db = this.userService.checkUser(user);
        return BaseResult.success();
    }

    @PostMapping(path = "/upload")
    @ResponseBody
    public BaseResult uploadPDF(@RequestParam("file")MultipartFile file) {
        return BaseResult.success();
    }

    @PostMapping(path = "/testQysException")
    public @ResponseBody
    BaseResult testQysException(){
        this.userService.testQysException();
        return BaseResult.success();
    }

    @PostMapping(path = "/testOtherException")
    public @ResponseBody
    BaseResult testOtherException(){
        this.userService.testOtherException();
        return BaseResult.success();
    }

}
