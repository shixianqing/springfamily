package com.example.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description: aop切面，在执行某个包下的方法时，切换数据源
 */
@Component
@Slf4j
@Aspect
public class DataSourceAop {

    @Before("execution(* com.example.primary.service..*.*(..))")
    public void setPrimaryDataSource(){
        DataSourceType.setDataSource(DataSourceType.DataBaseType.PRIMARY);
    }


    @Before("execution(* com.example.sub.service..*.*(..))")
    public void setSubDataSource(){
        DataSourceType.setDataSource(DataSourceType.DataBaseType.SUB);
    }
}


