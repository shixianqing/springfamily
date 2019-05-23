package com.example.mapperExt.controller;

import com.example.mapperExt.model.Coffee;
import com.example.mapperExt.service.MapperExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:sxq
 * @Date: 2019/5/23
 * @Description:
 */
@RestController
public class MapperExtController {

    @Autowired
    private MapperExtService service;


    @GetMapping("/save")
    public String save(){
        Coffee coffee = Coffee.builder()
                                .name("麦斯威尔咖啡")
                                .price(new BigDecimal("12.9").setScale(2,BigDecimal.ROUND_HALF_UP))
                                .createTime(new Date())
                                .build();

        Coffee coffee1 = Coffee.builder()
                .name("星巴克咖啡")
                .price(new BigDecimal("122.9").setScale(2,BigDecimal.ROUND_HALF_UP))
                .createTime(new Date())
                .build();

        service.saveCoffee(coffee);
        service.saveCoffee(coffee1);

        return "" + coffee.getId() + "----" + coffee1.getId();

    }

    @GetMapping("/get/{id}")
    public Coffee getById(@PathVariable(name = "id") Integer id){

        return service.getCoffeeById(id);

    }


}


