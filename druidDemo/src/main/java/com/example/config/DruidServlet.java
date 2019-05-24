package com.example.config;

import com.alibaba.druid.support.http.StatViewServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 13:41
 * @Description:
 **/
@WebServlet(urlPatterns = {"/druid/*"},initParams = {
        @WebInitParam(name = "loginPassword",value = "admin"),
        @WebInitParam(name = "loginPassword",value = "admin"),
        @WebInitParam(name = "resetEnable",value = "false")
})
public class DruidServlet extends StatViewServlet {

}
