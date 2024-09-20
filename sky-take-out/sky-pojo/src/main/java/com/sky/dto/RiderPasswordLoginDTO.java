package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:05
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手密码登录时，提交的数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderPasswordLoginDTO {
    // 登录时使用的电话号码
    private String phone;
    // 密码
    private String password;
}
