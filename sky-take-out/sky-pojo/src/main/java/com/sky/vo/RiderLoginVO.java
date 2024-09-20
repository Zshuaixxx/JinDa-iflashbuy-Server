package com.sky.vo;

/**
 * @author 帅的被人砍
 * @create 2024-09-20 14:01
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手登录返回接口数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderLoginVO {
    // 用户的唯一标识符
    private Long id;
    // 用户的OpenID
    private String openid;
    // 用户的访问令牌
    private String token;

}
