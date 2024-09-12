package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:29
 */
public interface OrderService {

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 查询历史订单  用户端
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);
}
