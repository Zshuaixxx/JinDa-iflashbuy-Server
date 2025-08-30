package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:19
 */
public interface CategoryService {

    /**
     * 分页查询分类信息
     *
     * @param categoryPageQueryDTO
     * @param merchantId
     * @return
     */
    PageResult getPageCate(CategoryPageQueryDTO categoryPageQueryDTO, Long merchantId);

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

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteCate(Long id);

    /**
     * 根据类型查询分类
     *
     * @param type
     * @param merchantId
     * @return
     */
    Category[] getCateListByType(Integer type, Long merchantId);
}
