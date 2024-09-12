package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-12 17:04
 */
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
}
