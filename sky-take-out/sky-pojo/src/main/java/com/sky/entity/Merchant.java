package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
    @TableField(exist = false)
    private String description;

    /**
     * 商家状态：0-未审核，1-审核通过，2-审核失败
     */
    private Integer status;

    /**
     * 营业执照
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 配送范围(km)
     */
    @TableField("delivery_scope")
    private Double deliveryRange;

    /**
     * 起送价
     */
    @TableField("min_order")
    private Double minOrderAmount;

    /**
     * 配送费
     */
    @TableField("delivery_fee")
    private Double deliveryFee;

    /**
     * 营业时间
     */
    @TableField("business_hours")
    private String businessHours;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;
}