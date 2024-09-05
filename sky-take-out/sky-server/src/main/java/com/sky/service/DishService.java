package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 批量删除菜品
     * @param ids
     */
    Result<String> deleteDishs(List<Long> ids);


    /**
     * 根据id查询菜品 包括菜品关联的口味
     * @param id
     * @return
     */
    DishVO getDishAndFlavorById(Long id);

    /**
     * 更新菜品及其口味信息
     * @param dishDTO
     */
    void updateDishAndFlavorById(DishDTO dishDTO);

    /**
     * 菜品启停售
     * @param status
     * @param id
     */
    void updateDishStatusById(Integer status, Long id);

    /**
     * 根据分类id查询对应菜品
     * @return
     */
    List<Dish> getDishByCategoryId(Long categoryId);
}
