package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-06 15:10
 */

/**
 * 商家端 店铺管理
 */
@RestController("adminShopController")
@Slf4j
public class ShopController {

    private static final String shopStatus= "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    @PutMapping("/admin/shop/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态{}",status);
        redisTemplate.opsForValue().set(shopStatus,status);
        return Result.success();
    }

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/admin/shop/status")
    public Result<Integer> getShopStatus(){
        Integer status= (Integer) redisTemplate.opsForValue().get(shopStatus);
        log.info("商家端获取店铺营业状态{}",status);
        return Result.success(status);
    }
}
