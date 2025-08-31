package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 根据id查询套餐 包括套餐对应的菜品信息
     * @param id
     * @return
     */
    SetmealVO getSetmealById(Integer id);


    /**
     * 修改更新套餐信息
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 套餐起停售
     * @param status
     * @param id
     */
    void updateSetmealStatus(Integer status, Integer id);

    /**
     * 批量删除套餐
     * @param ids
     */
    Result deleteSetmealsByIds(List<Integer> ids);

    /**
     * 根据分类id查询套餐信息
     *
     * @param categoryId
     * @return
     */
    Setmeal[] getSetmealByCategoryId(Long categoryId);
}
