package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nuist.common.Result;
import com.nuist.pojo.ShoppingCart;
import com.nuist.service.ShoppingCartService;
import jdk.nashorn.internal.runtime.SharedPropertyMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 查询购物车
     * @param session
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getList(HttpSession session) {
        Long id = (Long)session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, id);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(qw);
        return Result.success(shoppingCarts);
    }

    /**
     * 清空购物车
     * @param session
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart(HttpSession session) {
        Long id = (Long)session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, id);
        shoppingCartService.remove(qw);
        return Result.success("清空成功");
    }

    /**
     * 向购物车添加菜品/套餐
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public Result<String> addSetmeal(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long id = (Long)session.getAttribute("user");
        shoppingCart.setUserId(id);
        shoppingCartService.add(shoppingCart);
        return Result.success("添加成功");
    }

    /**
     * 购物车中减少菜品/套餐
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/sub")
    public Result<String> subSetmeal(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long id = (Long)session.getAttribute("user");
        shoppingCart.setUserId(id);
        shoppingCartService.sub(shoppingCart);
        return Result.success("添加成功");
    }
}
