package com.example.controller;

import com.example.model.Coffee;
import com.example.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author:shixianqing
 * @Date:2019/5/24 11:00
 * @Description:
 **/
@RestController
public class CoffeeController {

    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/test")
    public List<Coffee> test(){

        return coffeeService.selectAll();
    }

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

        coffeeService.saveCoffee(coffee);
        coffeeService.saveCoffee(coffee1);

        return "" + coffee.getId() + "----" + coffee1.getId();

    }
}
