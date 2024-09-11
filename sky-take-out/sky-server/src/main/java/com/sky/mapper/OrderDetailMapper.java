package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:34
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 向订单明细表中批量插入数据
     * @param orderDetailList
     */
    void insertList(List<OrderDetail> orderDetailList);
}
