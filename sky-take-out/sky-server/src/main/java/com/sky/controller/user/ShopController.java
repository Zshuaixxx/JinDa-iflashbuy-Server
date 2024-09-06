package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 15:19
 */

/**
 * 用户端 店铺管理
 */
@Slf4j
@RestController("userShopController")
public class ShopController {

    private static final String shopStatus= "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/user/shop/status")
    public Result getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(shopStatus);
        return Result.success(status);
    }
}
