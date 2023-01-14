package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);

    public void again(Long orderId);
}
