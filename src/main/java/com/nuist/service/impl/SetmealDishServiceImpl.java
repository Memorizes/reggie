package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.SetmealDishMapper;
import com.nuist.pojo.SetmealDish;
import com.nuist.service.SetmealDishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Override
    public void removeByList(List<Long> ids) {
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.in(SetmealDish::getSetmealId, ids);
        this.remove(qw);
    }
}
