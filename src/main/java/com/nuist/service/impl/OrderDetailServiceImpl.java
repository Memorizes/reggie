package com.nuist.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.OrderDetailMapper;
import com.nuist.pojo.OrderDetail;
import com.nuist.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService{
}
