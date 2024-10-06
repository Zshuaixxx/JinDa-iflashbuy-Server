package com.sky.dto;

/**
 * @author 帅的被人砍
 * @create 2024-09-22 15:56
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 骑手端订单广场查询参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderSquareOrderDTO {
    //页码
    private Integer page;
    //每页记录数
    private Integer pageSize;
    //骑手位置纬度
    private Double latitude;
    //骑手位置经度
    private Double longitude;
    //骑手所在行政区划编号
    private String adcode;
}
