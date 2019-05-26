package com.example.controller;

import com.example.common.response.MetaResponse;
import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sxq
 * @Date: 2019/5/26
 * @Description:
 */
@RestController
@Slf4j
public class LoginController {


    @PostMapping("/login")
    public MetaResponse login( User user){
        log.info("{}",user);
        return MetaResponse.success();
    }
}


