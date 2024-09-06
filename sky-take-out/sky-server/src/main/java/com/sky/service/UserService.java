package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

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
}
