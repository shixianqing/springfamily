package com.example.datasource.model;

import lombok.*;

/**
 * @Author:sxq
 * @Date: 2019/5/22
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    private Integer id;
    private String userName;
    private Integer userAge;
}


