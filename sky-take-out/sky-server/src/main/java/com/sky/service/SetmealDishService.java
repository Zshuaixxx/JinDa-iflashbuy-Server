package com.sky.service;

import com.sky.vo.DishItemVO;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 21:59
 */
public interface SetmealDishService {
    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    DishItemVO[] getDishBySetmealId(Long id);
}
