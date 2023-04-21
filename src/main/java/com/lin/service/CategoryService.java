package com.lin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}