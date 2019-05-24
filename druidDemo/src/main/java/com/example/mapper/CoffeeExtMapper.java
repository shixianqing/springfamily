package com.example.mapper;

import com.example.model.Coffee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:sxq
 * @Date: 2019/5/23
 * @Description:
 */
@Mapper
public interface CoffeeExtMapper extends CoffeeMapper{

    Coffee selectById(Integer id);
}
