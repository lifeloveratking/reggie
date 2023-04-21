package com.lin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}