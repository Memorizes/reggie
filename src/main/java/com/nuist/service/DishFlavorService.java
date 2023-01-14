package com.nuist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.pojo.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    public void removeByDishIdList(List<Long> ids);
}
