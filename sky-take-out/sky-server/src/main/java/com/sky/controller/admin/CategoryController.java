package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 帅的被人砍
 * @create 2024-09-03 16:16
 */

/**
*分类管理
*/
@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/admin/category/page")
    public Result<PageResult> getPageCate(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类信息{}",categoryPageQueryDTO);
        PageResult result=categoryService.getPageCate(categoryPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping("/admin/category")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类信息{}",categoryDTO);
        categoryService.addCategory(categoryDTO);
        return  Result.success();
    }

    /**
     * 设置分类状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/admin/category/status/{status}")
    public Result setCateStatus(@PathVariable String status,Long id){
        log.info("设置分类状态{}，{}",status,id);
        categoryService.setCateStatus(status,id);
        return Result.success();
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @PutMapping("/admin/category")
    public Result updateCate(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类信息{}",categoryDTO);
        categoryService.updateCate(categoryDTO);
        return Result.success();
    }
}
