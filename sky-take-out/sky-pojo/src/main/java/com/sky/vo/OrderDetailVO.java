package com.sky.vo;

/**
 * @author 帅的被人砍
 * @create 2024-10-08 15:25
 */

import com.sky.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 骑手查看订单详情VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailVO {
    // 订单id
    private Long id;
    // 订单号
    private String number;
    // 下单时间
    private LocalDateTime orderTime;
    // 结账时间
    private LocalDateTime checkoutTime;
    //实收金额
    private BigDecimal amount;
    //备注
    private String remark;
    //手机号
    private String phone;
    //地址
    private String address;
    //收货人
    private String consignee;
    //预计到达时间
    private LocalDateTime estimatedDeliveryTime;
    //配送状态
    private Integer deliveryStatus;
    //餐具数量状态
    private Integer tablewareStatus;
    //餐具数量
    private Integer tablewareNumber;
    //和骑手的距离
    private Double distance;
    //订单商品清单
    private List<OrderDetail> orderDetailList;
    //目的地经纬度
    private String location;
    //行政区划编码
    private String adcode;
    //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
    private Integer status;
    //骑手id
    private Long riderId;
}
