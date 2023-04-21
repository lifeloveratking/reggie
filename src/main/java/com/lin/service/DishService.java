package com.lin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.dto.DishDto;
import com.lin.entity.Dish;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);
    // 根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);
    // 更新菜品，以及口味信息
    public void updateWithFlavor(DishDto dishDto);
}