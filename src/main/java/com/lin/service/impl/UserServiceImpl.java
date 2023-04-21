package com.lin.service.impl;

import com.lin.entity.User;
import com.lin.mapper.UserMapper;
import com.lin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}