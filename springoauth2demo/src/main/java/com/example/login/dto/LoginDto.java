package com.example.login.dto;

import lombok.*;

/**
 * @Author:sxq
 * @Date: 2019/5/25
 * @Description:
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String userName;
    private String password;
}


