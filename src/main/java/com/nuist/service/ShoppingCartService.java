package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.mapper.ShoppingCartMapper;
import com.nuist.pojo.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    public void add(ShoppingCart shoppingCart);

    public void sub(ShoppingCart shoppingCart);
}
