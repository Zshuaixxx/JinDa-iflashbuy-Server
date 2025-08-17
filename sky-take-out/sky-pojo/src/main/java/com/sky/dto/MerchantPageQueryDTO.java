package com.sky.dto;

import lombok.Data;

/**
 * 商家分页查询DTO
 */
@Data
public class MerchantPageQueryDTO {

    /**
     * 页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家状态
     */
    private Integer status;

}