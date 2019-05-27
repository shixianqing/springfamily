package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.common.response.MetaResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:shixianqing
 * @Date:2019/5/27 10:14
 * @Description: 授权成功处理器
 **/
public class CustomAuthSuccessHanlder implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //设置token，返回给前端
        String s = JSONObject.toJSONString(MetaResponse.success());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().write(s.getBytes());
    }
}
