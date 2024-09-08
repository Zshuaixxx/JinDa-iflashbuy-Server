package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShopCartMapper;
import com.sky.service.ShopCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-07 20:33
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增购物车菜品
     * @param shoppingCartDTO
     */
    @Override
    public void addShopCartItem(ShoppingCartDTO shoppingCartDTO) {
        //先查询此菜品是否存在
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCarts=shopCartMapper.getShopCartItems(shoppingCart);
        //存在  数量加一
        if(shoppingCarts != null && shoppingCarts.size()>0){
            shoppingCarts.get(0).setNumber(shoppingCarts.get(0).getNumber()+1);
            shopCartMapper.updateShopCartItem(shoppingCarts.get(0));
        }else {//不存在 创建新的购物车item对象
            //新增的菜品还是套餐
            if(shoppingCart.getSetmealId() == null){//新增菜品
                Dish dish=dishMapper.getDishById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else {
                Setmeal setmeal= setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shopCartMapper.addShopCartItem(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> viewShoppingCart() {
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shopCartMapper.getShopCartItems(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShopCart() {
        Long userId = BaseContext.getCurrentId();
        shopCartMapper.cleanShopCartByUserId(userId);
    }

    /**
     * 删除购物车中某件菜品或者套餐
     */
    @Override
    public void deleteShopCartItem(ShoppingCartDTO shoppingCartDTO) {
        //先把这个菜品或者套餐查出来
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCarts=shopCartMapper.getShopCartItems(shoppingCart);
        if(shoppingCarts != null && shoppingCarts.size()>0){
            Integer number = shoppingCarts.get(0).getNumber();
            if(number == 1){
                //数量为1 -》 删除
                shopCartMapper.deleteShopCartItem(shoppingCarts.get(0));
            }
            else{
                // 数量大于1 -》 减一
                shoppingCarts.get(0).setNumber(number-1);
                shopCartMapper.updateShopCartItem(shoppingCarts.get(0));
            }
        }
    }

}
