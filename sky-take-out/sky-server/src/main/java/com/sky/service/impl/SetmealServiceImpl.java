package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.MerchantContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
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
        // 插入套餐表
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.addSetmeal(setmeal, MerchantContext.getCurrentId());

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

        Page<SetmealVO> page=setmealMapper.pageViewSetmeal(setmealPageQueryDTO, MerchantContext.getCurrentId());
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
        SetmealVO setmealVO=setmealMapper.getSetmealById(Long.valueOf(id));
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
        //起售前判断套餐内有无停售菜品
        if(status == StatusConstant.ENABLE){
            List<Dish> dishList=setmealMapper.getDishBySetmealId(id);
            if(dishList != null && dishList.size() > 0){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(Long.valueOf(id))
                .status(status)
                .build();
        setmealMapper.updateSetmeal(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public Result deleteSetmealsByIds(List<Integer> ids) {
        ids.forEach(id->{
            SetmealVO setmealVO=setmealMapper.getSetmealById(Long.valueOf(id));
            if(setmealVO.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        // TODO 优化为删除ids 一次sql语句执行完成
        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmaelId(Long.valueOf(setmealId));
        });

        return Result.success();
    }

    /**
     * 根据分类id查询套餐信息
     * @param categoryId
     * @param merchantId
     * @return
     */
    @Override
    public Setmeal[] getSetmealByCategoryId(Long categoryId, Long merchantId) {
        return setmealMapper.getSetmealByCategoryId(categoryId, merchantId);
    }
}
