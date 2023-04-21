package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.dto.OrdersDto;
import com.lin.dto.PageQueryDto;
import com.lin.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    public void submit(Orders orders);
    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    Page<OrdersDto> userPage(Integer page, Integer pageSize);
    /**
     * 分页多条件查询
     * @param pageQueryDto
     * @return
     */
    Page<Orders> queryPage(PageQueryDto pageQueryDto);

}