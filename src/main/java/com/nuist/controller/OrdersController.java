package com.nuist.controller;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.common.BaseContext;
import com.nuist.common.Result;
import com.nuist.dto.OrderDto;
import com.nuist.pojo.OrderDetail;
import com.nuist.pojo.Orders;
import com.nuist.service.OrderDetailService;
import com.nuist.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailService orderDetailService;

    /**
     *提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submitOrder(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return Result.success("订单提交成功");
    }


    /**
     * 查询所有订单
     * @return
     */
    @GetMapping("/list")
    public Result<List<Orders>> getList(){
        List<Orders> list = ordersService.list();
        return Result.success(list);
    }

    /**
     * 分页查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrderDto>> getPage(Integer page, Integer pageSize) {
        Long userId = BaseContext.getValue();
        Page<Orders> pageinfo = new Page<>(page, pageSize);
        Page<OrderDto> pageRet = new Page<>();
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<>();
        qw.eq(Orders::getUserId, userId);
        ordersService.page(pageinfo, qw);
        BeanUtils.copyProperties(pageinfo, pageRet, "records");
        List<Orders> orders = pageinfo.getRecords();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Orders order : orders) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            LambdaQueryWrapper<OrderDetail> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(OrderDetail::getOrderId, order.getNumber());
            orderDto.setOrderDetails(orderDetailService.list(qw1));
            orderDtos.add(orderDto);
        }
        pageRet.setRecords(orderDtos);
        return Result.success(pageRet);
    }


    /**
     * 再来一单，将订单详情写入购物车
     * @param
     * @return
     */
    @PostMapping("/again")
    public Result<String> again(@RequestBody Orders orders){
        Orders orders1 = ordersService.getById(orders.getId());
        ordersService.again(Long.valueOf(orders1.getNumber()));
        return Result.success("操作成功");
    }

    @GetMapping("/page")
    public Result<Page<Orders> > getPage(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        System.out.println("page => " + page);
        System.out.println("pageSize => " + pageSize);
        System.out.println("number => " + number);
        System.out.println("beginTime => " + beginTime);
        System.out.println("number=>" + endTime);
        Page<Orders> pageinfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<>();
        qw.like(null != number, Orders::getNumber, number);
        if(null != beginTime) {
            LocalDateTime beginTimeT = LocalDateTime.parse(beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            qw.ge(Orders::getCheckoutTime, beginTimeT);
        }
        if(null != endTime) {
            LocalDateTime endTimeT = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            qw.le(Orders::getCheckoutTime, endTimeT);
        }
        qw.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(pageinfo, qw);
        return Result.success(pageinfo);
    }
}
