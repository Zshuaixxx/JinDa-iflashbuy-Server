package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:20
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult getPageCate(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page =categoryMapper.getPageCate(categoryPageQueryDTO);
        long total=page.getTotal();
        List<Category> records=page.getResult();
        return new PageResult(total,records);
    }
}
