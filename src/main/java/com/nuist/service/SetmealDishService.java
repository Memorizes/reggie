package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.pojo.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    public void removeByList(List<Long> ids);
}
