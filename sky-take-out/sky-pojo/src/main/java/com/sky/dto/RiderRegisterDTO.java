package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 10:25
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手注册账号DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderRegisterDTO {
    //手机号
    private String phone;
    //密码
    private String password;
    //昵称
    private String name;
}
