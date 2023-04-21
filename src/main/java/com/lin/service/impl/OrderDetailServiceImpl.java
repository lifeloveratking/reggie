package com.lin.service.impl;

import com.lin.entity.OrderDetail;
import com.lin.mapper.OrderDetailMapper;
import com.lin.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}