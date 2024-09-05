package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 18:13
 */
@Mapper
public interface DishMapper {

    /**
     * 把要删除的分类下的菜品 归属到暂无分类
     * @param id
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update dish set category_id=1 where category_id=#{id}")
    void deleteCate(Long id);

    /**
     * 添加菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 分页查询菜品（支持三种条件）
     * @param page
     * @param pageSize
     * @param categoryId
     * @param name
     * @param status
     * @return
     */
    Page<DishVO> pageViewDish(String page, String pageSize, String categoryId, String name, String status);

    /**
     * 根据ids查询菜品
     * @param ids
     * @return
     */
    Dish[] getDishByIds(List<Long> ids);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteDishs(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getDishById(Long id);

    /**
     * 更新菜品信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);
}
