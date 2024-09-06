package com.sky.service.impl;

import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 21:59
 */
@Service
public class SetmealDishServiceImpl implements SetmealDishService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    @Override
    public DishItemVO[] getDishBySetmealId(Long id) {
        return setmealDishMapper.getDishItemBySetmealId(id);
    }
}
