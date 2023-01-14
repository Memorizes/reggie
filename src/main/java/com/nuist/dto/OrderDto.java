package com.nuist.dto;

import com.nuist.pojo.OrderDetail;
import com.nuist.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}
