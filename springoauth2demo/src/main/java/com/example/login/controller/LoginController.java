package com.example.login.controller;

import com.example.common.response.MetaResponse;
import com.example.login.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sxq
 * @Date: 2019/5/25
 * @Description:
 */
@RestController
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public MetaResponse login(@RequestBody LoginDto loginDto){

        log.info("{}",loginDto);
        return MetaResponse.success();
    }
}


