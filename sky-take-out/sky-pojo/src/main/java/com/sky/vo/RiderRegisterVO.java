package com.sky.vo;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 10:27
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手注册账号VO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RiderRegisterVO {
    //注册账号id
    private Long id;
}
