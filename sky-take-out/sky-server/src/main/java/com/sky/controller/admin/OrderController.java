package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
