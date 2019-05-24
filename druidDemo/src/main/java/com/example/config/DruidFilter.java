package com.example.config;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 14:01
 * @Description:
 **/
@WebFilter(urlPatterns = "/*",initParams = {
        @WebInitParam(name = "exclusions",value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico")
})
public class DruidFilter extends WebStatFilter {
}
