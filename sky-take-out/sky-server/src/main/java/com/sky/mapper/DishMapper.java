package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
}
