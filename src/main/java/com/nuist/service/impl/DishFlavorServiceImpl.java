package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.DishFlavorMapper;
import com.nuist.pojo.Dish;
import com.nuist.pojo.DishFlavor;
import com.nuist.service.DishFlavorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Override
    public void removeByDishIdList(List<Long> ids) {
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.in(DishFlavor::getDishId, ids);
        this.remove(qw);
    }
}
