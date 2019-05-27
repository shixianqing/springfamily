package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.common.response.MetaResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:shixianqing
 * @Date:2019/5/27 13:56
 * @Description:
 **/
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        request.getSession().invalidate();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        MetaResponse success = MetaResponse.success();
        response.getOutputStream().write(JSONObject.toJSONString(success).getBytes());
    }
}
