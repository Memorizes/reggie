package com.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuist.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
