package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:20
 */
@Mapper
public interface CategoryMapper {

    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> getPageCate(CategoryPageQueryDTO categoryPageQueryDTO);
}
