package com.nuist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuist.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
