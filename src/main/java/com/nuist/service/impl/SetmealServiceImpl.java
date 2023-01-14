package com.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.common.CustomerException;
import com.nuist.dto.SetmealDto;
import com.nuist.mapper.SetmealMapper;
import com.nuist.pojo.Dish;
import com.nuist.pojo.Setmeal;
import com.nuist.pojo.SetmealDish;
import com.nuist.service.SetmealDishService;
import com.nuist.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveBySetmealDto(SetmealDto setmealDto) {
        this.saveOrUpdate(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        for(SetmealDish setmealDish : setmealDishList) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 删除ids
     * @param ids
     */
    @Override
    public void removeByList(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        int count = this.count(qw);
        if(count > 0) {
            throw new CustomerException("有套餐未停售，无法删除");
        }
        LambdaQueryWrapper<Setmeal> qw1 = new LambdaQueryWrapper<>();
        qw1.in(Setmeal::getId, ids);
        this.remove(qw1);
        setmealDishService.removeByList(ids);
    }

}
