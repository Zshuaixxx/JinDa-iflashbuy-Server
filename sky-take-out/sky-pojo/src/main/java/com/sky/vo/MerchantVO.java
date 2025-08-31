package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户端商家信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家logo
     */
    private String logo;

    /**
     * 商家描述（使用地址信息）
     */
    private String description;

    /**
     * 预计配送时间（分钟）
     */
    private Integer deliveryTime;

    /**
     * 配送费
     */
    private Double deliveryFee;

    /**
     * 起送价
     */
    private Double minOrderAmount;

    /**
     * 距离用户当前位置距离（公里）
     */
    private Double distance;

    /**
     * 营业时间
     */
    private String businessHours;
}