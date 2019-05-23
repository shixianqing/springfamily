package com.example.datasource.controller;

import com.example.datasource.model.User;
import com.example.datasource.primary.service.PrimaryUserService;
import com.example.datasource.sub.service.SubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@RestController
public class TestController {

    @Autowired
    private PrimaryUserService primaryUserService;

    @Autowired
    private SubUserService subUserService;

    @GetMapping("/test")
    public String test(){
        User user1 = User.builder().userName("张三").userAge(12).build();
        User user2 = User.builder().userName("李四").userAge(44).build();

        primaryUserService.saveUser(user1);
        subUserService.saveUser(user2);

        return "SUCCESS";
    }
}


