package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.RiderSquareOrderDTO;
import com.sky.entity.Orders;
import com.sky.temp.OrdersAndLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * 管理端查询历史订单
     *
     * @param ordersPageQueryDTO
     * @param merchantId
     * @return
     */
    Page<Orders> merchant_pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO, Long merchantId);


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
    @Select("select count(id) from orders where status=#{status} and merchant_id=#{merchantId}")
    Integer countStatus(Integer status, Long merchantId);

    /**
     * 查询超时订单
     * @param pendingPayment
     * @param time
     * @return
     */
    @Select("select * from orders where  status=#{pendingPayment} and order_time < #{time}")
    List<Orders> getTimeOutOrder(Integer pendingPayment, LocalDateTime time);


    /**
     * 根据日期和订单状态查询营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 骑手端订单广场查询订单 page pageSize latitude longitude adcode
     * @param riderSquareOrderDTO
     * @return
     */
    Page<OrdersAndLocation> pageViewOrderByAdcode(RiderSquareOrderDTO riderSquareOrderDTO);

    /**
     * 骑手接单
     * @param orderId
     * @param riderId
     * @param status
     */
    void riderTakeOrder(Long orderId, Long riderId, Integer status, Integer version);

    /**
     * 根据订单id查询订单经纬度
     * @param orderId
     * @return
     */
    OrdersAndLocation getOrderLocationById(Long orderId);

    /**
     * 用户支付订单
     * @param orderNumber
     * @param payMethod
     * @param now
     */
    void payOrder(String orderNumber, Integer payMethod, LocalDateTime now);

    /**
     * 根据骑手id查询正在配送中的订单
     * @param riderId
     * @return
     */
    List<OrdersAndLocation> getGoingOrder(Long riderId);

    /**
     * 更新订单信息
     * @param orders 订单实体类
     */
    void updateOrder(Orders orders);

    /**
     * 统计骑手时间段内的收益
     * @param map
     * @return
     */
    BigDecimal sumRiderTodayIncome(Map map);

    /**
     * 商家接单时设置乐观锁版本号
     * @param id
     * @param i
     */
    @Update("update orders set version=#{i} where id=#{id} ")
    void setVersion(Long id, int i);
}
