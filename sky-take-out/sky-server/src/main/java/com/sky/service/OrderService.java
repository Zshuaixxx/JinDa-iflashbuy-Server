package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

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

    /**
     * 查询历史订单 管理端
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult admin_pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);
}
