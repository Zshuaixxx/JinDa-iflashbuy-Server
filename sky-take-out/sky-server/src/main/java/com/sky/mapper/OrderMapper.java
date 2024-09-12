package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderPageViewVO;
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


    /**
     * 更新订单状态
     * @param orderNumber
     * @param status
     */
    void updateOrderStatus(String orderNumber, int status);


    /**
     * 用户端查询历史订单 userid status page pagesize
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);
}
