package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author 帅的被人砍
 * @create 2024-09-05 22:45
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     */
    @AutoFill(OperationType.INSERT)
    @Transactional
    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {
        // 插入套餐表   TO DO 需要返回id
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.addSetmeal(setmeal);

        //插入套餐菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(item ->{
            item.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.addSetmealDish(setmealDishes);
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageViewSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page=setmealMapper.pageViewSetmeal(setmealPageQueryDTO);
        long total=page.getTotal();
        List<SetmealVO> records=page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 根据id查询套餐 包括套餐对应的菜品信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getSetmealById(Integer id) {
        //现查套餐 再查菜品
        SetmealVO setmealVO=setmealMapper.getSetmealById(id);
        setmealVO.setSetmealDishes(setmealDishMapper.getSetmealDishBySetmealId(id));
        return setmealVO;
    }

    /**
     * 修改更新套餐信息
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //套餐表
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateSetmeal(setmeal);

        //套餐菜品关系表 先删 后增
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null  && setmealDishes.size()>0){
            setmealDishes.forEach(item ->{
                item.setSetmealId(setmeal.getId());
            });
            setmealDishMapper.deleteBySetmaelId(setmeal.getId());
            setmealDishMapper.addSetmealDish(setmealDishes);
        }
    }

    /**
     * 套餐起停售
     * @param status
     * @param id
     */
    @AutoFill(OperationType.UPDATE)
    @Override
    public void updateSetmealStatus(Integer status, Integer id) {
        Setmeal setmeal = Setmeal.builder()
                .id(Long.valueOf(id))
                .status(status)
                .build();
        setmealMapper.updateSetmeal(setmeal);
    }
}
