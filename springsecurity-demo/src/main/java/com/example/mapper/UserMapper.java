package com.example.mapper;

import com.example.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@Mapper
public interface UserMapper {

    User loadUserByUsername(String username);
}
