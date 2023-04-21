package com.lin.service.impl;

import com.lin.entity.AddressBook;
import com.lin.mapper.AddressBookMapper;
import com.lin.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}