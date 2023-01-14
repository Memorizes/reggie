package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.common.CustomerException;
import com.nuist.mapper.ShoppingCartMapper;
import com.nuist.pojo.ShoppingCart;
import com.nuist.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.security.provider.SHA;

import java.math.BigDecimal;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public void add(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(null != shoppingCart.getSetmealId(), ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(null != shoppingCart.getDishId(), ShoppingCart::getDishId, shoppingCart.getDishId());
        ShoppingCart shoppingCart1 = this.getOne(qw1);
        if(null == shoppingCart1) {
            this.save(shoppingCart);
        }
        else {
            shoppingCart.setNumber(shoppingCart1.getNumber() + 1);
            LambdaUpdateWrapper<ShoppingCart> uw = new LambdaUpdateWrapper<>();
            uw.eq(ShoppingCart::getUserId, shoppingCart.getUserId()).eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            this.update(shoppingCart, uw);
        }
    }

    @Override
    public void sub(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(null != shoppingCart.getSetmealId(), ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(null != shoppingCart.getDishId(), ShoppingCart::getDishId, shoppingCart.getDishId());
        ShoppingCart shoppingCart1 = this.getOne(qw1);
        if(null == shoppingCart1) {
            throw new CustomerException("购物车中该套餐数目为0");
        }
        else {
            shoppingCart.setNumber(shoppingCart1.getNumber() - 1);
            if(shoppingCart.getNumber() != 0) {
                LambdaUpdateWrapper<ShoppingCart> uw = new LambdaUpdateWrapper<>();
                uw.eq(ShoppingCart::getUserId, shoppingCart.getUserId()).eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
                this.update(shoppingCart, uw);
            }
            else {
                this.remove(qw1);
            }
        }
    }
}
