package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.*;

import java.time.LocalDate;
import java.util.List;

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
    PageResult merchant_pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 用户取消订单
     * @param id
     */
    void cancelOrder(Long id);

    /**
     * 用户再来一单
     * @param id
     */
    void againOrder(Long id);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void complete(Long id);

    /**
     * 用户催单
     * @param id
     */
    void reminderOrder(Long id);

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 骑手订单广场查询订单
     * @param riderSquareOrderDTO
     * @return
     */
    PageResult pageViewRiderSquareOredr(RiderSquareOrderDTO riderSquareOrderDTO);

    /**
     * 骑手接单
     * @param orderId
     */
    void riderTakeOrder(Long orderId);

    /**
     * 骑手查看订单详情
     * @param orderDetailDTO
     * @return
     */
    OrderDetailVO viewOrderDetail(OrderDetailDTO orderDetailDTO);

    /**
     * 骑手查看配送中的订单
     * @param riderSquareOrderDTO
     * @return
     */
    List<RiderSquareOrderVO> riderGoingOrder(RiderSquareOrderDTO riderSquareOrderDTO);

    /**
     * 骑手上传送达凭证
     * @param deliveryProofDTO
     */
    void deliveryProof(DeliveryProofDTO deliveryProofDTO);
}
