package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealDishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private SetmealDishMapper setmealDishMapper;

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

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public Result<String> deleteDishs(List<Long> ids) {
        //判断是否有起售中的菜品
        Dish[] dishs=dishMapper.getDishByIds(ids);
        for (int i = 0; i < dishs.length; i++) {
            if(dishs[i].getStatus() == StatusConstant.ENABLE){
                return Result.error(dishs[i].getName() + "启售中 无法删除");
            }
        }
        //判断是否有菜品关联套餐
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishByDishids(ids);
        if(setmealDishes != null && setmealDishes.size()>0){
            return Result.error(setmealDishes.get(0).getName()+"关联套餐 无法删除");
        }
        //删除菜品和关联的口味
        dishMapper.deleteDishs(ids);
        dishFlavorMapper.deleteDishFlavor(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据菜品id查询菜品 包括菜品关联的口味
     * @param id
     * @return
     */
    @Override
    public DishVO getDishAndFlavorById(Long id) {
        Dish dish=dishMapper.getDishById(id);
        List<DishFlavor> dishFlavors=dishFlavorMapper.getFloverByDishId(id);

        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 更新菜品及其口味信息
     * @param dishDTO
     */
    @Override
    public void updateDishAndFlavorById(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);

        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        if(dishFlavors != null && dishFlavors.size()>0){
            //先删除  再插入
            dishFlavorMapper.deleteDishFlavor(Collections.singletonList(dishDTO.getId()));
            List<DishFlavor> flavors = dishDTO.getFlavors();
            if(flavors != null && flavors.size() > 0){
                flavors.forEach(flavor -> {
                    flavor.setDishId(dishDTO.getId());
                });
                dishFlavorMapper.addDishFlavor(flavors);
            }
        }
    }

    /**
     * 菜品启停售
     * @param status
     * @param id
     */
    @Override
    public void updateDishStatusById(Integer status, Long id) {
        Dish dish = Dish.builder().status(status)
                                .id(id)
                                .build();
        dishMapper.updateDish(dish);
    }

    /**
     * 根据分类id查询对应菜品
     * @return
     */
    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

}
