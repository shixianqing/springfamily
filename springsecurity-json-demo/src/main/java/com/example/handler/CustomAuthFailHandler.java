package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.common.response.MetaResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:shixianqing
 * @Date:2019/5/27 11:12
 * @Description:
 * 授权失败处理器
 **/
public class CustomAuthFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String message = exception.getMessage();
        String respMsg = JSONObject.toJSONString(MetaResponse.error("用户名或密码不正确！"));
        response.getOutputStream().write(respMsg.getBytes());
    }
}
