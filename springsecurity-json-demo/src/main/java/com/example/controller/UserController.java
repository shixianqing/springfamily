package com.example.controller;

import com.example.common.response.MetaResponse;
import com.example.mapper.UserMapper;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:shixianqing
 * @Date:2019/5/27 14:27
 * @Description:
 **/
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    public MetaResponse list(){
        List<User> users = userMapper.selectAll();
        return MetaResponse.success(users);
    }
}
