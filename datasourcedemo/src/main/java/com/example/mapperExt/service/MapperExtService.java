package com.example.mapperExt.service;

import com.example.mapperExt.mapper.CoffeeExtMapper;
import com.example.mapperExt.mapper.CoffeeMapper;
import com.example.mapperExt.model.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:sxq
 * @Date: 2019/5/23
 * @Description:
 */
@Service
public class MapperExtService {

    @Autowired
    private CoffeeExtMapper coffeeExtMapper;

    public List<Coffee> getAllCoffees(){

        return coffeeExtMapper.selectAll();
    }

    public void saveCoffee(Coffee coffee){
        coffeeExtMapper.insert(coffee);
    }

    public Coffee getCoffeeById(Integer id){
        return coffeeExtMapper.selectById(id);
    }
}


