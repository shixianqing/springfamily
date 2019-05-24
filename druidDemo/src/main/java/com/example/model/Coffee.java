package com.example.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:sxq
 * @Date: 2019/5/23
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Coffee {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
}


