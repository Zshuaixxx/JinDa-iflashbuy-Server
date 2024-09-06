package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 22:01
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询
     * @param ids
     * @return
     */
    List<SetmealDish> getSetmealDishByDishids(List<Long> ids);

    /**
     * 插入套餐对应的菜品列表
     * @param setmealDishes
     */
    void addSetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 查询套餐id对应的菜品列表
     * @param id
     * @return
     */
    List<SetmealDish> getSetmealDishBySetmealId(Integer id);
}
