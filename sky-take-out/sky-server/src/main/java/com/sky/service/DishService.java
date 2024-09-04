package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.result.PageResult;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 17:35
 */
public interface DishService {

    /**
     * 添加菜品
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * 分页查询菜品（支持三种条件）
     * @param page
     * @param pageSize
     * @param categoryId
     * @param name
     * @param status
     * @return
     */
    PageResult pageViewDish(String page, String pageSize, String categoryId, String name, String status);
}
