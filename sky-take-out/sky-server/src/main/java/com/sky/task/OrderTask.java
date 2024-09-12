package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-12 21:38
 */
@Slf4j
@Component
public class OrderTask {


    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单超市
     */
    @Scheduled(cron = "0 * * * * ? ")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void orderPayOutTimeTask(){
        log.info("处理支付超时订单{}", LocalDateTime.now());

        LocalDateTime time =LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList= orderMapper.getTimeOutOrder(Orders.PENDING_PAYMENT,time);

        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.updateOrderStatusById(orders.getId(),Orders.CANCELLED);
            }
        }
    }

    /**
     * 处理派送未点松达订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void orderDeliveryTimeOutTask(){
        log.info("处理派送未点松达订单{}",LocalDateTime.now());
        LocalDateTime time =LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList= orderMapper.getTimeOutOrder(Orders.DELIVERY_IN_PROGRESS,time);

        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.updateOrderStatusById(orders.getId(),Orders.COMPLETED);
            }
        }
    }
}
