package com.example.datasource.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description: 当切换数据源时，将目标数据源放到线程副本里
 */
@Slf4j
public class DataSourceType {

    enum DataBaseType {
        PRIMARY,SUB;
    }

    private static final ThreadLocal<DataBaseType> THREADLOCAL = new ThreadLocal<>();

    public static void setDataSource(DataBaseType type){
        THREADLOCAL.set(type);
        log.info("数据源切换到：{}",type);
    }

    public static DataBaseType getDataSource(){
        return THREADLOCAL.get();
    }

    public static void removeDataSource(){
        THREADLOCAL.remove();
    }
}


