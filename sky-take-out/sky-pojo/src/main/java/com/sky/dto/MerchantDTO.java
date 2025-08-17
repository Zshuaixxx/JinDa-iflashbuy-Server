package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 商家数据传输对象
 */
@Data
public class MerchantDTO implements Serializable {

    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 商家logo
     */
    private String logo;

    /**
     * 商家简介
     */
    private String description;

    /**
     * 配送范围(km)
     */
    private Double deliveryRange;

    /**
     * 起送价
     */
    private Double minOrderAmount;

    /**
     * 配送费
     */
    private Double deliveryFee;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 营业执照
     */
    private String businessLicense;

}