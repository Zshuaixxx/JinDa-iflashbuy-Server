package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-10-09 21:28
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传送达凭证DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryProofDTO {
    /** 订单id */
    private Long orderId;
    /** 送达凭证 */
    private String deliveryProof;
}
