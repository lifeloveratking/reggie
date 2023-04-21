package com.lin.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.common.R;
import com.lin.dto.OrdersDto;
import com.lin.entity.Orders;
import com.lin.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    /**
     * 提交订单
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> userPage(Integer page, Integer pageSize){
        Page<OrdersDto> dtoPage = ordersService.userPage(page, pageSize);
        return R.success(dtoPage);
    }
    /**
     * 再来一单
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        Orders temp = ordersService.getById(orders.getId());
        temp.setId(null);
        temp.setStatus(2);
        long orderId = IdWorker.getId(); // 订单号
        temp.setNumber(String.valueOf(orderId));
        temp.setOrderTime(LocalDateTime.now());
        temp.setCheckoutTime(LocalDateTime.now());
        ordersService.save(temp);
        return R.success("下单成功");
    }
    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPages(Integer page, Integer pageSize){
        Page<OrdersDto> dtoPage = ordersService.userPage(page, pageSize);
        return R.success(dtoPage);
    }
    /**
     * 更新订单状态
     * @param orders
     * @return
     */
    @PutMapping()
    public R<String> toSend(@RequestBody Orders orders){
        log.info("派送订单：{}",orders.toString());
        ordersService.updateById(orders);
        return R.success("派送成功");
    }
}