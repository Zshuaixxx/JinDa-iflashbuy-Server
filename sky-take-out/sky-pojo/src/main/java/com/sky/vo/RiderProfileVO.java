package com.sky.vo;

/**
 * @author 帅的被人砍
 * @create 2024-10-10 21:43
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 骑手简要信息VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderProfileVO {

    /** 骑手id*/
    private Long id;
    /** 骑手名称*/
    private String name;
    /** 头像*/
    private String avatar;
    /**今日收益*/
    private BigDecimal todayIncome;
    /**当月收益*/
    private BigDecimal monthIncome;
}
