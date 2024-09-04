package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 17:35
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 添加菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //插入菜品
        dishMapper.addDish(dish);
        //插入口味
        Long id=dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(flavor -> {
                flavor.setDishId(id);
            });
            dishFlavorMapper.addDishFlavor(flavors);
        }
    }

    /**
     * 分页查询菜品（支持三种条件）
     * @param page
     * @param pageSize
     * @param categoryId
     * @param name
     * @param status
     * @return
     */
    @Override
    public PageResult pageViewDish(String page, String pageSize, String categoryId, String name, String status) {
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(pageSize));
        Page<DishVO> pageResult=dishMapper.pageViewDish(page,pageSize,categoryId,name,status);
        long total=pageResult.getTotal();
        List<DishVO> records=pageResult.getResult();
        return new PageResult(total,records);
    }
}
