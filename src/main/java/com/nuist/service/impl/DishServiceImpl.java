package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.common.CustomerException;
import com.nuist.dto.DishDto;
import com.nuist.mapper.DishMapper;
import com.nuist.pojo.Dish;
import com.nuist.pojo.DishFlavor;
import com.nuist.service.DishFlavorService;
import com.nuist.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithDishDto(DishDto dishDto) {
        this.saveOrUpdate(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        for(DishFlavor dishFlavor : dishFlavors) {
            dishFlavor.setDishId(id);
        }
        dishFlavorService.saveBatch(dishFlavors);
    }

    /**
     * 根据ids删除菜品，先判断是否有菜品状态未停售，如果可以删除要把对应的口味也删除
     * @param ids
     */
    @Override
    public void removeList(List<Long> ids) {
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = this.count(qw);
        if(count > 0) {
            throw new CustomerException("有菜品未停售，无法删除");
        }
        LambdaQueryWrapper<Dish> qw2 = new LambdaQueryWrapper<>();
        qw2.in(Dish::getId, ids);
        this.remove(qw2);
        dishFlavorService.removeByDishIdList(ids);
    }
}
