package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 18:13
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量添加口味数据
     * @param flavors
     */
    void addDishFlavor(List<DishFlavor> flavors);
}
