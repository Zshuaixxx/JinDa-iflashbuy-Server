package com.sky.controller.admin;

import com.sky.annotation.Log;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Set;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 17:34
 */

/**
 * 菜品管理
 */
@Slf4j
@RestController
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @Log
    @PostMapping("/admin/dish")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishService.addDish(dishDTO);
        cleanCache("dish_*");
        return Result.success();
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
    @GetMapping("/admin/dish/page")
    public Result<PageResult> pageViewDish(String page,String pageSize,String categoryId,String name,String status){
        log.info("分页查询菜品：{},{},{},{},{}",page,pageSize,categoryId,name,status);
        PageResult pageResult=dishService.pageViewDish(page,pageSize,categoryId,name,status);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @Log
    @DeleteMapping("/admin/dish")
    public Result<String> deleteDishs(@RequestParam List<Long> ids){
        log.info("批量删除菜品{}",ids);
        cleanCache("dish_*");
        return dishService.deleteDishs(ids);
    }

    /**
     * 根据id查询菜品 包括菜品关联的口味
     * @return
     */
    @GetMapping("/admin/dish/{id}")
    public Result<DishVO> getDishAndFlavorById(@PathVariable Long id){
        log.info("根据id查询菜品：{}",id);
        DishVO dishVO=dishService.getDishAndFlavorById(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品及其口味信息
     * @param dishDTO
     * @return
     */
    @Log
    @PutMapping("/admin/dish")
    public Result updateDishAndFlavorById(@RequestBody DishDTO dishDTO){
        log.info("更新菜品信息{}",dishDTO);
        dishService.updateDishAndFlavorById(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 菜品启停售
     * @param status
     * @param id
     * @return
     */
    @Log
    @PostMapping("/admin/dish/status/{status}")
    public Result updateDishStatusById(@PathVariable Integer status,Long id){
        log.info("菜品启停售{}{}",status,id);
        dishService.updateDishStatusById(status,id);
        cleanCache("dish_*");
        return Result.success();
    }


    /**d
     * 根据分类id查询对应菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/admin/dish/list")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId){
        log.info("根据分类id查询对应菜品{}",categoryId);
        List<Dish> dishes=dishService.getDishByCategoryId(categoryId);
        return Result.success(dishes);
    }

    /**
     * 清理redis缓存
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
