package com.sky.entity;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:32
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 骑手实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rider {
    /**
     * 骑手ID
     */
    private Long id;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户的OpenID
     */
    private String openid;

    /**
     * 用户的注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 用户的性别信息。0位置 1男 2女
     */
    private Integer sex;

    /**
     * 用户的头像地址。
     */
    private String avatar;

}
