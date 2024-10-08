package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 帅的被人砍
 * @create 2024-09-12 13:26
 */

/**
 * 用户支付相关接口
 */
@RestController
public class PayController {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 订单支付   此处仅模拟支付  直接修改数据库对应订单状态
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/user/order/payment")
    public Result pay(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        orderMapper.payOrder(ordersPaymentDTO.getOrderNumber(),ordersPaymentDTO.getPayMethod(), LocalDateTime.now());

        //向管理端发送来单提醒
        Map map = new HashMap();
        map.put("type",1);//1表示来单提醒2表示客户催单
        map.put("orderId",ordersPaymentDTO.getOrderNumber());
        map.put("content","订单号："+ ordersPaymentDTO.getOrderNumber());
        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);

        return Result.success();
    }
}
