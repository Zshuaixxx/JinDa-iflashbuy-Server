package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 帅的被人砍
 * @create 2024-09-12 17:04
 */
@Slf4j
@RestController("adminOrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 管理端分页查询历史订单
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/admin/order/conditionSearch")
    public Result<PageResult> PageViewHistoryOrder(OrdersPageQueryDTO ordersPageQueryDTO){
        return Result.success(orderService.admin_pageViewHistoryOrders(ordersPageQueryDTO));
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @GetMapping("/admin/order/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 管理端查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/admin/order/details/{id}")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        log.info("管理端查看订单详情：{}",id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 商家接单
     *
     * @return
     */
    @PutMapping("/admin/order/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @return
     */
    @PutMapping("/admin/order/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @return
     */
    @PutMapping("/admin/order/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     *
     * @return
     */
    @PutMapping("/admin/order/delivery/{id}")
    public Result delivery(@PathVariable("id") Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @return
     */
    @PutMapping("/admin/order/complete/{id}")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
