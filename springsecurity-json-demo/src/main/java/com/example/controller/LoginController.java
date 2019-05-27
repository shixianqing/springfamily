package com.example.controller;

import com.example.common.response.MetaResponse;
import com.example.model.User;
import com.example.service.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sxq
 * @Date: 2019/5/26
 * @Description:
 */
@RestController
@Slf4j
public class LoginController {

    @Autowired
    UserDetailServiceImpl userDetailService;
    @PostMapping("/login")
    public MetaResponse login(@RequestBody User user){
        log.info("{}",user);
        return MetaResponse.success();
    }

    @PostMapping("/registry")
    public MetaResponse registry(@RequestBody User user){

        userDetailService.registry(user);
        return MetaResponse.success();
    }


//    @PostMapping("/loginout")
//    public MetaResponse loginout(){
//
//        return MetaResponse.success();
//    }
}


