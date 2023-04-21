package com.lin.service.impl;

import com.lin.entity.ShoppingCart;
import com.lin.mapper.ShoppingCartMapper;
import com.lin.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}