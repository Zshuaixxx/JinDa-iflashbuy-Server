package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:19
 */
public interface CategoryService {

    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult getPageCate(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 设置分类状态
     * @param status
     * @param id
     */
    void setCateStatus(String status, Long id);

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    void updateCate(CategoryDTO categoryDTO);
}
