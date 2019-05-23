package com.example.mapperExt.mapper;

import com.example.mapperExt.model.Coffee;

import java.util.List;

/**
 * @Author:sxq
 * @Date: 2019/5/23
 * @Description:
 */
public interface CoffeeMapper {

    List<Coffee> selectAll();
    int insert(Coffee coffee);
}
