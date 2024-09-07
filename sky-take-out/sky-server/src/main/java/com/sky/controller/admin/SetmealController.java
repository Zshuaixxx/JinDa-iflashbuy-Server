package com.sky.controller.admin;

/**
 * @author 帅的被人砍
 * @create 2024-09-05 22:44
 */

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache" ,key = "#setmealDTO.categoryId")
    @PostMapping("/admin/setmeal")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐{}",setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/admin/setmeal/page")
    public Result<PageResult> pageViewSetmeal(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询套餐{}",setmealPageQueryDTO);
        return Result.success(setmealService.pageViewSetmeal(setmealPageQueryDTO));
    }

    /**
     * 根据id查询套餐 包括套餐对应的菜品信息
     * @param id
     * @return
     */
    @GetMapping("/admin/setmeal/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Integer id){
        log.info("根据id查询套餐",id);
        return Result.success(setmealService.getSetmealById(id));
    }

    /**
     * 修改更新套餐信息
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PutMapping("/admin/setmeal")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起停售
     * @param status
     * @param id
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PostMapping("/admin/setmeal/status/{status}")
    public Result updateSetmealStatus(@PathVariable Integer status,Integer id){
        log.info("套餐起停售{}{}",status,id);
        setmealService.updateSetmealStatus(status,id);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @DeleteMapping("/admin/setmeal")
    public Result deleteSetmealsByIds(@RequestParam List<Integer> ids){
        log.info("批量删除套餐{}",ids);
        Result result=setmealService.deleteSetmealsByIds(ids);
        return result;
    }
}
