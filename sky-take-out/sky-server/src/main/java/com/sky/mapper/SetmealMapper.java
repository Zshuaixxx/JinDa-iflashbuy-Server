package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

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
    void addSetmeal(Setmeal setmeal);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageViewSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);
}
