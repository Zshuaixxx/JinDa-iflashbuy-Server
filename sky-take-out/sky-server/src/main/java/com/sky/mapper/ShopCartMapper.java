package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-07 20:34
 */
@Mapper
public interface ShopCartMapper {
    /**
     * 查询购物车数据  用户id 菜品id  套餐id 口味
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> getShopCartItems(ShoppingCart shoppingCart);

    /**
     * 购物车中菜品数量更新
     * @param shoppingCart
     */
    @Update("update shopping_cart set number =#{number} where id=#{id}")
    void updateShopCartItem(ShoppingCart shoppingCart);

    /**
     * 向购物车表插入数据
     * @param shoppingCart
     */
    void addShopCartItem(ShoppingCart shoppingCart);

    /**
     * 根据userId删除用户的购物车数据
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void cleanShopCartByUserId(Long userId);
}
