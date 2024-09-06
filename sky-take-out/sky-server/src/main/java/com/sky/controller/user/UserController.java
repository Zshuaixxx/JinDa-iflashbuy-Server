package com.sky.controller.user;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 17:26
 */

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.User;
import com.sky.mapper.DishMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.DishVO;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关接口
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 用户端登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/user/user/login")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户微信登录{}",userLoginDTO);

        //微信登录
        User user = userService.wxlogin(userLoginDTO);

        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginvo = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginvo);
    }

    /**
     * 用户端查询分类和套餐信息
     * @param type
     * @return
     */
    @GetMapping("/user/category/list")
    public Result<Category[]> getCategoryByType(Integer type){
        log.info("用户端查询分类和套餐信息{}",type);
        Category[] categories= categoryService.getCateListByType(type);
        return Result.success(categories);
    }

    /**
     * 查询分类id下的所有菜品和对应的口味
     * @param categoryId
     * @return
     */
    @GetMapping("/user/dish/list")
    public Result<DishVO[]> getDishAndFlavorsByCategoryId(Long categoryId){
        DishVO[] dishVOS=userService.getDishAndFlavorsByCategoryId(categoryId);
        return Result.success(dishVOS);
    }
}
