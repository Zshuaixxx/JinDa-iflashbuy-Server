package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.DishVO;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 17:48
 */
public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxlogin(UserLoginDTO userLoginDTO);

    /**
     * 查询分类id下的所有菜品和对应的口味
     * @param categoryId
     * @return
     */
    DishVO[] getDishAndFlavorsByCategoryId(Long categoryId);
}
