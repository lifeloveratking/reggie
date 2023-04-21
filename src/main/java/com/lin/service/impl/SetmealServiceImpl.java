package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.R;
import com.lin.dto.DishDto;
import com.lin.dto.SetmealDto;
import com.lin.entity.Category;
import com.lin.entity.Setmeal;
import com.lin.entity.SetmealDish;
import com.lin.mapper.SetmealMapper;
import com.lin.service.CategoryService;
import com.lin.service.SetmealDishService;
import com.lin.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes=setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }
    @Transactional
    @Override
    public SetmealDto SelectById(Long id) {
        Setmeal setmeal=this.getById(id);
        Long category_id=setmeal.getCategoryId();

        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list=setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐的信息
        this.updateById(setmealDto);
        //删除套餐中所关联的菜品
        Long setmeal_id=setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmeal_id);
        setmealDishService.remove(wrapper);
        List<SetmealDish> list=setmealDto.getSetmealDishes();
        list=list.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);


    }
}