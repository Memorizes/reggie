package com.nuist.dto;

import com.nuist.pojo.Setmeal;
import com.nuist.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
