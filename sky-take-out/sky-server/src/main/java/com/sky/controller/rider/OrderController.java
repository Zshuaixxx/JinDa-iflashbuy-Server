package com.sky.controller.rider;

import com.sky.dto.RiderSquareOrderDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 帅的被人砍
 * @create 2024-09-21 17:15
 */

/**
 * 骑手端订单管理
 */
@Slf4j
@RestController("riderOrderController")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 骑手订单广场查询订单
     * @param riderSquareOrderDTO
     * @return
     */
    @PostMapping("/rider/order/square")
    public Result<PageResult> pageViewRiderSquareOredr (@RequestBody RiderSquareOrderDTO riderSquareOrderDTO){
        log.info("骑手端订单广场查询参数：{}",riderSquareOrderDTO);
        return Result.success(orderService.pageViewRiderSquareOredr(riderSquareOrderDTO));
    }
}
