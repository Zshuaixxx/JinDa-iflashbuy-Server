package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.impl.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-07 20:29
 */

/**
 * 用户端购物车管理
 */
@RestController
@Slf4j
public class ShopCartController {

    @Autowired
    ShopCartService shopCartService;

    /**
     * 用户添加菜品到购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/user/shoppingCart/add")
    public Result addShopCartItem(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("新增购物车{}",shoppingCartDTO);
        shopCartService.addShopCartItem(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/user/shoppingCart/list")
    public Result<List<ShoppingCart>> viewShoppingCart(){
        return Result.success(shopCartService.viewShoppingCart());
    }

}
