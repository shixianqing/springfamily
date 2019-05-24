package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 10:12
 * @Description:
 **/
@SpringBootApplication
@ServletComponentScan("com.example.config")
public class DruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class,args);
    }
}
