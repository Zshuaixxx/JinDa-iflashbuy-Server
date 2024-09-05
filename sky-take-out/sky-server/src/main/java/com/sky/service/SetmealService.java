package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author 帅的被人砍
 * @create 2024-09-05 22:45
 */
public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageViewSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);
}
