package com.example.datasource.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description: 多数据员配置
 */
//@Configuration
//@MapperScan(sqlSessionFactoryRef = "sqlSessionFactory",basePackages = {"com.example.mapper"})
public class MultiDataSourceConfig {


    @Primary
    @Bean("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.primary.datasource")
    public DataSource primaryDataSource(){
        return DataSourceBuilder.create().build();
    }


    @Bean("subDataSource")
    @ConfigurationProperties(prefix = "spring.sub.datasource")
    public DataSource subDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource,
                                        @Qualifier("subDataSource") DataSource subDataSource){
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceType.DataBaseType.PRIMARY,primaryDataSource);
        targetDataSource.put(DataSourceType.DataBaseType.SUB,subDataSource);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSource);

        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource);

        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
}


