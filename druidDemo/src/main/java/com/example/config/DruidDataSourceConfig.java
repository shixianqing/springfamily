package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 10:33
 * @Description:
 **/
@Configuration
public class DruidDataSourceConfig {


    /**
     * ConfigurationProperties 注解可以将配置文件中定义的属性自动封装到实体中
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

}
