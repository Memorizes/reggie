package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.common.BaseContext;
import com.nuist.mapper.OrdersMapper;
import com.nuist.pojo.*;
import com.nuist.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Address;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    AddressBookService addressBookService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Transactional
    @Override
    public void submit(Orders orders) {
        Long userId = BaseContext.getValue();

        Long orderId = IdWorker.getId();
        orders.setNumber(orderId.toString());
        orders.setUserId(userId);

        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());

        User user = userService.getById(userId);
        orders.setUserName(user.getName());

        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(4);
        orders.setPayMethod(1);

        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(qw);
        BigDecimal amount = new BigDecimal(0);
        for(ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            Integer nNumber = shoppingCart.getNumber();
            orderDetail.setNumber(nNumber);
            BigDecimal nAmount = shoppingCart.getAmount();
            orderDetail.setAmount(nAmount);
            amount = amount.add(nAmount.multiply(new BigDecimal(nNumber)));
            orderDetailService.save(orderDetail);
        }
        orders.setAmount(amount);
        this.save(orders);

        LambdaQueryWrapper<ShoppingCart> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(qw);
    }

    @Override
    public void again(Long orderId) {
        Long userId = BaseContext.getValue();
        LambdaQueryWrapper<OrderDetail> qw = new LambdaQueryWrapper<>();
        qw.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = orderDetailService.list(qw);

        LambdaQueryWrapper<ShoppingCart> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(qw1);

        for(OrderDetail orderDetail : orderDetails) {
            ShoppingCart temp = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, temp, "id", "orderId");
            temp.setUserId(userId);
            temp.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(temp);
        }
    }
}
