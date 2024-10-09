package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.*;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShopCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.temp.OrdersAndLocation;
import com.sky.utils.QQmapUtil;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 帅的被人砍
 * @create 2024-09-11 13:30
 */
@Slf4j
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
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private QQmapUtil qqmapUtil;

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
        orders.setAddress(addressBook.getDetail());

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

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    public OrderVO details(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getDetailByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 用户取消订单
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        orderMapper.updateOrderStatusById(id,6);
    }

    /**
     * 用户再来一单
     * @param id
     */
    @Override
    public void againOrder(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getDetailByOrderId(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shopCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    public OrderStatisticsVO statistics() {
        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 将查询出的数据封装到orderStatisticsVO中响应
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.updateOrderStatusById(orders.getId(),Orders.CONFIRMED);
    }

    /**
     * 拒单
     * @param ordersRejectionDTO DTo
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        orderMapper.updateOrderStatusById(ordersDB.getId(),Orders.CANCELLED);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());


        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrderStatusById(orders.getId(),Orders.CANCELLED);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    public void delivery(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.updateOrderStatusById(orders.getId(),Orders.DELIVERY_IN_PROGRESS);
    }

    /**
     * 完成订单
     *
     * @param id
     */
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.updateOrderStatusById(orders.getId(),Orders.COMPLETED);
    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void reminderOrder(Long id) {
        Orders orders = orderMapper.getById(id);
        //向管理端发送催单提醒
        Map map = new HashMap();
        map.put("type",2);//1表示来单提醒2表示客户催单
        map.put("orderId",id);
        map.put("content","订单号："+orders.getNumber());
        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //TODO  sql优化
        //存放日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天的营业额
        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date:dateList){
            //查询date日期对应的营业额数据，营业额是指：状态为“已完成”的订单金额合计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            //select sum(amount)from orders where order_time>beginTime and order_time<endTimeand status=5
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,','))
                .turnoverList(StringUtils.join(turnoverList,','))
                .build();
    }

    /**
     * 骑手订单广场查询订单
     * @param riderSquareOrderDTO
     * @return
     */
    @Override
    public PageResult pageViewRiderSquareOredr(RiderSquareOrderDTO riderSquareOrderDTO) {
        //根据adcode分页查询出附近的订单
        PageHelper.startPage(riderSquareOrderDTO.getPage(),riderSquareOrderDTO.getPageSize());
        Page<OrdersAndLocation> pageResult=orderMapper.pageViewOrderByAdcode(riderSquareOrderDTO);
        long total=pageResult.getTotal();
        List<OrdersAndLocation> records=pageResult.getResult();
        //计算出距离和时间
        String from = riderSquareOrderDTO.getLatitude()+","+riderSquareOrderDTO.getLongitude();
        List<String > allTO=new ArrayList<>();
        for (OrdersAndLocation ordersAndLocation:records){
            allTO.add(ordersAndLocation.getLocation());
        }
        String to=StringUtils.join(allTO,';');
        String mode="bicycling";
        List<List<Double>> distanceAndDuration = qqmapUtil.getDistance(from, to, mode);
        //返回订单列表
        List<RiderSquareOrderVO> ans=new ArrayList<>();
        //算力不足
        if (distanceAndDuration.isEmpty()){
            distanceAndDuration.add(Arrays.asList(0.0,0.0));
            for (int i=0;i<records.size();i++){
                ans.add(new RiderSquareOrderVO(records.get(i),distanceAndDuration.get(0)));
            }
        }else{
            for (int i=0;i<records.size();i++){
                ans.add(new RiderSquareOrderVO(records.get(i),distanceAndDuration.get(i)));
            }
        }
        return PageResult.builder()
                .total(total)
                .records(ans)
                .build();
    }

    //TODO 线程锁的优化
    /**
     * 骑手接单
     * @param orderId
     */
    @Override
    public synchronized void riderTakeOrder(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        //判断订单状态是否为待接单
        if(!orders.getStatus().equals(Orders.CONFIRMED)){
            throw new OrderBeenTaken(MessageConstant.Order_Been_Taken);
        }
        Long riderId=BaseContext.getCurrentId();
        orderMapper.riderTakeOrder(orderId,riderId,Orders.DELIVERY_IN_PROGRESS);
    }


    /**
     * 骑手查看订单详情
     * @param orderDetailDTO
     * @return
     */
    @Override
    public OrderDetailVO viewOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrdersAndLocation ordersAndLocation = orderMapper.getOrderLocationById(orderDetailDTO.getOrderId());
        if (ordersAndLocation != null) {
            OrderDetailVO orderDetailVO =new OrderDetailVO();
            BeanUtils.copyProperties(ordersAndLocation,orderDetailVO);
            //计算距离
            String from = orderDetailDTO.getLatitude()+","+orderDetailDTO.getLongitude();
            String to=ordersAndLocation.getLocation();
            String mode="bicycling";
            List<List<Double>> distanceAndDuration = qqmapUtil.getDistance(from, to, mode);
            orderDetailVO.setDistance(distanceAndDuration.get(0).get(0));
            //菜品详情
            List<OrderDetail> orderDetailList = orderDetailMapper.getDetailByOrderId(orderDetailDTO.getOrderId());
            orderDetailVO.setOrderDetailList(orderDetailList);
            return orderDetailVO;
        }else{
            throw new OrderNotFoundException(MessageConstant.ORDER_NOT_FOUND);
        }
    }

    /**
     * 骑手查看配送中的订单
     * @param riderSquareOrderDTO
     * @return
     */
    @Override
    public List<RiderSquareOrderVO> riderGoingOrder(RiderSquareOrderDTO riderSquareOrderDTO) {
        Long riderId=BaseContext.getCurrentId();
        //查询骑手正在配送中的订单
        List<OrdersAndLocation> ordersAndLocations = orderMapper.getGoingOrder(riderId);
        //计算距离
        String from = riderSquareOrderDTO.getLatitude()+","+riderSquareOrderDTO.getLongitude();
        List<String > allTO=new ArrayList<>();
        for (OrdersAndLocation ordersAndLocation:ordersAndLocations){
            allTO.add(ordersAndLocation.getLocation());
        }
        String to=StringUtils.join(allTO,';');
        String mode="bicycling";
        List<List<Double>> distanceAndDuration = qqmapUtil.getDistance(from, to, mode);
        //返回订单列表
        List<RiderSquareOrderVO> ans=new ArrayList<>();
        //算力不足
        if (distanceAndDuration.isEmpty()){
            distanceAndDuration.add(Arrays.asList(0.0,0.0));
            for (int i=0;i<ordersAndLocations.size();i++){
                ans.add(new RiderSquareOrderVO(ordersAndLocations.get(i),distanceAndDuration.get(0)));
            }
        }else{
            for (int i=0;i<ordersAndLocations.size();i++){
                ans.add(new RiderSquareOrderVO(ordersAndLocations.get(i),distanceAndDuration.get(i)));
            }
        }
        return ans;
    }

    /**
     * 骑手上传送达凭证
     * @param deliveryProofDTO
     */
    @Override
    public void deliveryProof(DeliveryProofDTO deliveryProofDTO) {
        //判断订单 存在 派送中 骑手id是否一致
        Orders orders = orderMapper.getById(deliveryProofDTO.getOrderId());
        if (orders == null){
            throw new OrderNotFoundException(MessageConstant.ORDER_NOT_FOUND);
        }
        Long riderId=BaseContext.getCurrentId();
        if (!orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS) || !orders.getRiderId().equals(riderId)){
            throw new OrderNotFoundException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setDeliveryProof(deliveryProofDTO.getDeliveryProof());
        orderMapper.updateOrder(orders);
    }
}
