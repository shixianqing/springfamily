package com.example.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType.DataBaseType dataBaseType = DataSourceType.getDataSource();
        log.info("当前使用的数据源是：{}",dataBaseType);
        return dataBaseType;
    }
}


