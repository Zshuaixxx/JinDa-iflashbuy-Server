package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:34
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入新的订单数据
     * @param orders
     */
    void insertOrder(Orders orders);
}
