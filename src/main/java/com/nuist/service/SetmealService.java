package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.dto.SetmealDto;
import com.nuist.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveBySetmealDto(SetmealDto setmealDto);

    public void removeByList(List<Long> ids);
}
