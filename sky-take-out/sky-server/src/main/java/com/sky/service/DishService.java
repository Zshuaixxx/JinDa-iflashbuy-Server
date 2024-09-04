package com.sky.service;

import com.sky.dto.DishDTO;

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
}
