package com.sky.vo;

import com.sky.temp.OrdersAndLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-22 16:10
 */

/**
 * 骑手订单广场查询订单VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderSquareOrderVO {
    //订单id
    private Long id;
    //订单号
    private String number;
    //订单实收金额
    private BigDecimal amount;
    //订单备注
    private String remark;
    //订单详细地址
    private String address;
    //预计送达时间
    private LocalDateTime estimatedDeliveryTime;
    //距离骑手距离
    private Double distance;


    public RiderSquareOrderVO(OrdersAndLocation ordersAndLocation, List<Double> doubles) {
        this.id = ordersAndLocation.getId();
        this.number = ordersAndLocation.getNumber();
        this.amount = ordersAndLocation.getAmount();
        this.remark = ordersAndLocation.getRemark();
        this.address = ordersAndLocation.getAddress();
        this.estimatedDeliveryTime = ordersAndLocation.getEstimatedDeliveryTime();
        this.distance = doubles.get(0);
    }
}
