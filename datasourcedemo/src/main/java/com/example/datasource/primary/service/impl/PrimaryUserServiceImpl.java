package com.example.datasource.primary.service.impl;

import com.example.datasource.mapper.UserMapper;
import com.example.datasource.model.User;
import com.example.datasource.primary.service.PrimaryUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@Service
@Slf4j
public class PrimaryUserServiceImpl implements PrimaryUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveUser(User user) {
        int row = userMapper.save(user);
        log.info("插入主数据源影响行数：{}",row);
    }
}


