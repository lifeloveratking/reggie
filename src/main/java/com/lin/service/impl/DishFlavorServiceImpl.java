package com.lin.service.impl;

import com.lin.entity.DishFlavor;
import com.lin.mapper.DishFlavorMapper;
import com.lin.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}