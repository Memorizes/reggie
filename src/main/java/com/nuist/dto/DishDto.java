package com.nuist.dto;

import com.nuist.pojo.Dish;
import com.nuist.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
    //private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
