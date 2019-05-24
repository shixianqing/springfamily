package com.example.mapper;


import com.example.model.Coffee;

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
