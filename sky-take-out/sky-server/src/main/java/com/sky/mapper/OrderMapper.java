package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderPageViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:34
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入新的订单数据
     * @param orders
     */
    void insertOrder(Orders orders);


    /**
     * 根据订单号更新订单状态
     * @param orderNumber
     * @param status
     */
    void updateOrderStatus(String orderNumber, int status);


    /**
     * 用户端查询历史订单 userid status page pagesize
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据id更新订单状态
     * @param id
     * @param i
     */
    @Update("update orders set status=#{i} where id=#{id}")
    void updateOrderStatusById(Long id, int i);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
