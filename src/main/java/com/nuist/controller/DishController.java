package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.common.Result;
import com.nuist.dto.DishDto;
import com.nuist.dto.SetmealDto;
import com.nuist.pojo.Category;
import com.nuist.pojo.Dish;
import com.nuist.pojo.DishFlavor;
import com.nuist.service.CategoryService;
import com.nuist.service.DishFlavorService;
import com.nuist.service.DishService;
import com.nuist.service.SetmealService;
import com.nuist.service.impl.DishServiceImpl;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.ClassNameMBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    /**
     * 查询菜品分页数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<DishDto> > getPage(Integer page, Integer pageSize, String name) {
        Page<Dish> pageinfo = new Page<>(page, pageSize);
        Page<DishDto> pageret = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.like(null != name, Dish::getName, name);
        dishService.page(pageinfo, qw);
        BeanUtils.copyProperties(pageinfo, pageret,"records");
        List<Dish> dishList = pageinfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        for(Dish dish : dishList) {
            DishDto temp = new DishDto();
            BeanUtils.copyProperties(dish, temp);
            temp.setCategoryName(categoryService.getById(temp.getCategoryId()).getName());
            dishDtos.add(temp);
        }
        pageret.setRecords(dishDtos);
        return Result.success(pageret);
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> saveDish(@RequestBody DishDto dishDto) {
        dishService.saveWithDishDto(dishDto);
        return Result.success("添加成功");
    }

    /**
     * 根据菜品id删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public Result<String> deleteDish(List<Long> ids) {
        dishService.removeList(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据菜品id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getDish(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        Long dishId = dish.getId();
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> list = dishFlavorService.list(qw);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(list);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    @Transactional
    public Result<String> updateDish(@RequestBody DishDto dishDto) {
        List<Long> ids = new ArrayList<>();
        ids.add(dishDto.getId());
        dishFlavorService.removeByDishIdList(ids);
        this.saveDish(dishDto);
        return Result.success("修改成功");
    }

    /**
     * 根据分类id查询所有菜品
     * @param categoryId
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto> > getList(Long categoryId, String name) {
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(null != categoryId, Dish::getCategoryId, categoryId);
        qw.like(null != name, Dish::getName, name);
        List<Dish> dishList = dishService.list(qw);
        List<DishDto> dishDtoList = new ArrayList<>();
        for(Dish dish : dishList) {
            DishDto temp = new DishDto();
            BeanUtils.copyProperties(dish, temp);
            LambdaQueryWrapper<DishFlavor> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(null != dish.getId(), DishFlavor::getDishId, dish.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(qw1);
            temp.setFlavors(dishFlavors);
            dishDtoList.add(temp);
        }
        return Result.success(dishDtoList);
    }

    /**
     * 根据菜品id批量修改菜品在售/停售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable Integer status,
                                       @RequestParam List<Long> ids) {
        System.out.println(ids);
        LambdaUpdateWrapper<Dish> uw = new LambdaUpdateWrapper<>();
        uw.in(Dish::getId, ids).set(Dish::getStatus, status);
        dishService.update(uw);
        return Result.success("操作成功");
    }
}
