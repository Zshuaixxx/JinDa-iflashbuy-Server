package com.sky.service.impl;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-07 20:32
 */
public interface ShopCartService {

    /**
     * 新增购物车菜品
     * @param shoppingCartDTO
     */
    void addShopCartItem(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> viewShoppingCart();
}
