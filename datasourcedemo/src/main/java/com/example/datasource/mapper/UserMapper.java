package com.example.datasource.mapper;

import com.example.datasource.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@Mapper
public interface UserMapper {


    int save(User user);
}
