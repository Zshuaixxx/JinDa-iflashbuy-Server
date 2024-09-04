package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-04 17:34
 */
@Slf4j
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("/admin/dish")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishService.addDish(dishDTO);
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
}
