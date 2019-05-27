package com.example.filter;

import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:shixianqing
 * @Date:2019/5/27 9:26
 * @Description: 重写 UsernamePasswordAuthenticationFilter 的attemptAuthentication方法，
 * 适配json报文
 **/
@Slf4j
public class CustomAuthenicationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String contentType = request.getContentType();
        Authentication authentication;
        ObjectMapper mapper = new ObjectMapper();
        UsernamePasswordAuthenticationToken authRequest = null;
        if (contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE) ||
                contentType.equals(MediaType.APPLICATION_JSON_VALUE)){

            try {
                ServletInputStream inputStream = request.getInputStream();
                User user = mapper.readValue(inputStream, User.class);
                authRequest = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                setDetails(request,authRequest);
                authentication = getAuthenticationManager().authenticate(authRequest);
            }
        } else {
            authentication = super.attemptAuthentication(request, response);
        }


        return authentication;
    }

}
