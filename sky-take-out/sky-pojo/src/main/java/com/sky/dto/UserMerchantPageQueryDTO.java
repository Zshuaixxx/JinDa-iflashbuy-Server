package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户端商家分页查询DTO
 */
@Data
public class UserMerchantPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 排序方式
     */
    private String sortBy;

    /**
     * 用户纬度
     */
    private Double latitude;

    /**
     * 用户经度
     */
    private Double longitude;

    /**
     * 搜索关键词
     */
    private String keyword;
}