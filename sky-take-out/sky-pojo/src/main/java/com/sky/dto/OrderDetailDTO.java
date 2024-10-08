package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-10-08 15:22
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 骑手获取订单详情DTO
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    //订单id
    private Long orderId;
    //骑手位置纬度
    private Double latitude;
    //骑手位置经度
    private Double longitude;
}
