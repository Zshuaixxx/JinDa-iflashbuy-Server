package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.EmployeeContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private DishMapper dishMapper;
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

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(0);
        // 设置商家ID，从上下文中获取
        category.setMerchantId(EmployeeContext.getCurrentId());
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.addCategory(category);
    }

    /**
     * 设置分类状态
     * @param status
     * @param id
     */
    @Override
    public void setCateStatus(String status, Long id) {
        Category category = Category.builder()
                .status(Integer.valueOf(status))
                .id(id)
//                .updateUser(BaseContext.getCurrentId())
//                .updateTime(LocalDateTime.now())
                .build();
        categoryMapper.updateCate(category);
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @Override
    public void updateCate(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        // 设置商家ID，从上下文中获取，防止被修改
        category.setMerchantId(EmployeeContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCate(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteCate(Long id) {
        dishMapper.deleteCate(id);
        categoryMapper.deleteCate(id);
    }

    /**
     * 根据类型查询菜品分类或套餐
     * @param type
     * @return
     */
    @Override
    public Category[] getCateListByType(Integer type) {
        return categoryMapper.getCateListByType(type);
    }
}
