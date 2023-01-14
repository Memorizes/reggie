package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.dto.DishDto;
import com.nuist.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    public void saveWithDishDto(DishDto dishDto);

    public void removeList(List<Long> ids);
}
