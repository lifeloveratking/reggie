package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.common.BaseContext;
import com.lin.common.CustomException;
import com.lin.dto.OrdersDto;
import com.lin.dto.PageQueryDto;
import com.lin.entity.*;
import com.lin.mapper.OrdersMapper;
import com.lin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 提交订单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        log.info("订单数据：{}", orders);
        // 用户id
        Long userId = BaseContext.getCurrentId();

        // 购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }

        // 查询用户数据
        User user = userService.getById(userId);

        // 地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if (addressBook == null){
            throw new CustomException("地址信息不能为空！不能下单");
        }

        // 订单表添加数据
        long orderId = IdWorker.getId(); // 订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setUserName(addressBook.getPhone());
        orders.setNumber(String.valueOf(orderId));
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);

        // 订单表明细添加数据
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<OrdersDto> userPage(Integer page, Integer pageSize) {
        // 分页构造器
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);

        this.page(ordersPage,ordersLambdaQueryWrapper);

        Page<OrdersDto> ordersDtoPage = new Page<>();

        // 分页的ordersDtoPage，没有records
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");

        // 构造orderDetails
        List<Orders> ordersList = ordersPage.getRecords();

        List<OrdersDto> ordersDtoList = ordersList.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            // 订单id
            String orderNum = item.getNumber();
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orderNum);

            BeanUtils.copyProperties(item, ordersDto);
            List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtoList);
        return ordersDtoPage;
    }
    /**
     * 分页多条件查询
     * @param pageQueryDto
     * @return
     */
    @Override
    public Page<Orders> queryPage(PageQueryDto pageQueryDto) {
        // 解构pageQueryDto
        int page = pageQueryDto.getPage();
        int pageSize = pageQueryDto.getPageSize();
        String number = pageQueryDto.getNumber();

        // 订单 分页构造器
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        // 订单 条件构造器
        LambdaQueryWrapper<Orders> ordersQueryWrapper = new LambdaQueryWrapper<>();
        // 根据订单时间，倒序排列
        ordersQueryWrapper.orderByDesc(Orders::getOrderTime);

        // 条件 订单号模糊查询
        ordersQueryWrapper.like(number != null, Orders::getNumber, number);

        // 判空
        if (pageQueryDto.getBeginTime() != null && pageQueryDto.getEndTime() != null){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime beginTime = LocalDateTime.parse(pageQueryDto.getBeginTime(), df);
            LocalDateTime endTime = LocalDateTime.parse(pageQueryDto.getEndTime(), df);
            // 条件 时间区间
            ordersQueryWrapper.between(Orders::getOrderTime, beginTime, endTime);
        }
        // 执行查询
        this.page(ordersPage, ordersQueryWrapper);

        return ordersPage;
    }
}