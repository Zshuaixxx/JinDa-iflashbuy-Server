package com.sky.service.impl;

import com.sky.dto.ShoppingCartDTO;

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
}
