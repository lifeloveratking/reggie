package com.lin.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.dto.DishDto;
import com.lin.dto.SetmealDto;
import com.lin.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
     void saveWithDish(SetmealDto setmealDto);
    SetmealDto SelectById(Long id);
    void updateWithDish(SetmealDto setmealDto);
}