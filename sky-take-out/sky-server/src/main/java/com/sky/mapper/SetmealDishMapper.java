package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
     *
     * @param setmealDishes
     */
    void addSetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 查询套餐id对应的菜品列表
     * @param id
     * @return
     */
    List<SetmealDish> getSetmealDishBySetmealId(Integer id);

    /**
     * 根据套餐id删除对应菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteBySetmaelId(Long id);


    /**
     * 根据分类id查询套餐信息
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id=#{categoryId}")
    Setmeal[] getSetmealByCategoryId(Long categoryId);

    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    DishItemVO[] getDishItemBySetmealId(Long id);
}
