package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.common.CustomerException;
import com.nuist.mapper.CategoryMapper;
import com.nuist.pojo.Category;
import com.nuist.pojo.Dish;
import com.nuist.pojo.Setmeal;
import com.nuist.service.CategoryService;
import com.nuist.service.DishService;
import com.nuist.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    public void remove(Long id) {
        LambdaQueryWrapper<Dish> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(qw1);
        if(count1 > 0) {
            throw new CustomerException("当前分类关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> qw2 = new LambdaQueryWrapper<>();
        qw2.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(qw2);
        if(count2 > 0) {
            throw new CustomerException("当前分类关联了套餐，不能删除");
        }
        super.removeById(id);
    }

}
