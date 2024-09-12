package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-12 13:26
 */
@RestController
public class PayController {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 订单支付   此处仅模拟支付  直接修改数据库对应订单状态
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/user/order/payment")
    public Result pay(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        orderMapper.updateOrderStatus(ordersPaymentDTO.getOrderNumber(),2);
        return Result.success();
    }
}
