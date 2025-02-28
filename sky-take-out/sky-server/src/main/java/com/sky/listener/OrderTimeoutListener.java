package com.sky.listener;

import com.sky.config.RabbitMQConfig;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 帅的被人砍
 * @create 2025-02-27 13:41
 */

/**
 * 订单超时监听器
 */
@Slf4j
@Service
public class OrderTimeoutListener {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 监听订单是否超时未支付
     * @param orderId
     */
    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
    public void handleOrderTimeout(Long orderId) {
        // 查询订单信息
        Orders orders = orderMapper.getById(orderId);
        log.info("消息队列检查订单是否超时：{}", orders.getId());

        if (orders != null && orders.getStatus() == Orders.PENDING_PAYMENT) {
            // 处理订单超时逻辑
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("订单支付超时，自动取消");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.updateOrder(orders);
        }
    }
}
