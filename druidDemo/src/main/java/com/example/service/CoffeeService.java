package com.example.service;

import com.example.mapper.CoffeeExtMapper;
import com.example.model.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 11:01
 * @Description:
 **/
@Service
public class CoffeeService {

    @Autowired
    private CoffeeExtMapper coffeeExtMapper;

    public List<Coffee> selectAll(){
        return coffeeExtMapper.selectAll();
    }

    public void saveCoffee(Coffee coffee) {
        coffeeExtMapper.insert(coffee);
    }
}
