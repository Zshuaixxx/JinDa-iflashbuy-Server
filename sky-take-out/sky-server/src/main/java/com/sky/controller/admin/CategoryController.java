package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
