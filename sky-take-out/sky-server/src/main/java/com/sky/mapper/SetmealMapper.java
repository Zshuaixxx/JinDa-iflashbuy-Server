package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-05 22:46
 */
@Mapper
public interface SetmealMapper {
    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal, Long merchantId);

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @param merchantId
     * @return
     */
    Page<SetmealVO> pageViewSetmeal(SetmealPageQueryDTO setmealPageQueryDTO, Long merchantId);

    /**
     * 根据套餐id查询套餐和对应的分类名称
     * @param id
     * @return
     */
    SetmealVO getSetmealById(Long id);

    /**
     * 更新套餐表信息
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);

    /**
     * 根据套餐id查询对应菜品
     * @param id
     * @return
     */
    List<Dish> getDishBySetmealId(Integer id);

    /**
     * 根据id删除套餐
     * @param setmealId
     */
    @Delete("delete from setmeal where id=#{setmealId}")
    void deleteById(Integer setmealId);

    @Select("select * from setmeal where id=#{setmealId}")
    Setmeal getById(Long setmealId);

    /**
     * 根据分类id查询套餐信息（用户端）
     * @param categoryId
     * @param merchantId
     * @return
     */
    @Select("select * from setmeal where category_id=#{categoryId} and merchant_id=#{merchantId} and status=1")
    Setmeal[] getSetmealByCategoryId(Long categoryId, Long merchantId);

}
