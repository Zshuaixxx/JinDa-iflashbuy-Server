package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShopCartMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPageViewVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.admin_OrderPageViewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:30
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO) {

        //判断地址是否存在
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw  new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //购物车是否为空
        Long id = BaseContext.getCurrentId();
        ShoppingCart s = ShoppingCart.builder()
                .id(id).build();
        List<ShoppingCart> shopCartItems = shopCartMapper.getShopCartItems(s);
        if(shopCartItems == null || shopCartItems.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //像订单表插入数据
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(id);

        orderMapper.insertOrder(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //3.向订单明细表插入n条数据
        for(ShoppingCart cart:shopCartItems){
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertList(orderDetailList);

        //删除用户的购物车数据
        shopCartMapper.cleanShopCartByUserId(id);

        //5.封装V0返回结果
        OrderSubmitVO orderSubmitVO=OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }

    /**
     * 查询历史订单  用户端 order orderDetail查询
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());

        //先查订单表
        Page<Orders> page =orderMapper.pageViewHistoryOrders(ordersPageQueryDTO);
        //再查orderDetail
        List<Orders> ordersList = page.getResult();
        List<OrderPageViewVO> ans=new ArrayList<>();
        ordersList.forEach(orders -> {
            OrderPageViewVO vo=new OrderPageViewVO();
            BeanUtils.copyProperties(orders,vo);
            vo.setOrderDetailList(orderDetailMapper.getDetailByOrderId(vo.getId()));
            ans.add(vo);
        });

        long total=page.getTotal();
        return new PageResult(total,ans);
    }

    /**
     * 查询历史订单  管理端
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult admin_pageViewHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());

        //先查订单表
        Page<Orders> page =orderMapper.pageViewHistoryOrders(ordersPageQueryDTO);

        //再查orderDetail
        List<Orders> ordersList = page.getResult();
        List<admin_OrderPageViewVO> ans=new ArrayList<>();
        ordersList.forEach(orders -> {
            admin_OrderPageViewVO vo=new admin_OrderPageViewVO();
            BeanUtils.copyProperties(orders,vo);
            List<OrderDetail> detail = orderDetailMapper.getDetailByOrderId(vo.getId());
            StringBuilder orderDishes = new StringBuilder();
            detail.forEach(orderDetail -> {
                orderDishes.append(orderDetail.getName()).append(",");
            });
            String result = orderDishes.toString();
            vo.setOrderDishes(result);
            ans.add(vo);
        });

        long total=page.getTotal();
        return new PageResult(total,ans);
    }
}
