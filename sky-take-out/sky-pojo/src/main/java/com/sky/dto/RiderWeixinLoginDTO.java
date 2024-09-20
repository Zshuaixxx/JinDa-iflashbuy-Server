package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 20:57
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手端微信登录时传递的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderWeixinLoginDTO {
    /**
     * 微信授权码
     */
    private String code;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 姓名
     */
    private String name;
}
