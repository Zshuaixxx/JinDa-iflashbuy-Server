package com.sky.controller.user;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:22
 */

import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户端订单管理
 */
@Slf4j
@RestController("userOrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/user/order/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.orderSubmit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户查询历史订单 page pageSize status userId
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/user/order/historyOrders")
    public Result<PageResult> pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("用户端查询历史订单：{}",ordersPageQueryDTO);
        //用户端查询需加入userid  管理端无需
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        return Result.success(orderService.pageViewHistoryOrders(ordersPageQueryDTO));
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/user/order/orderDetail/{id}")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        log.info("用户查看订单详情：{}",id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 用户取消订单
     * @param id
     * @return
     */
    @PutMapping("/user/order/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id){
        log.info("用户取消订单：{}",id);
        orderService.cancelOrder(id);
        return Result.success();
    }

    /**
     * 用户在来一单
     * @param id
     * @return
     */
    @PostMapping("/user/order/repetition/{id}")
    public Result againOrder(@PathVariable Long id){
        log.info("用户再来一单：{}",id);
        orderService.againOrder(id);
        return Result.success();
    }
}
