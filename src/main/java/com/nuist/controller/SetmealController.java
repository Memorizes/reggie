package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.common.Result;
import com.nuist.dto.DishDto;
import com.nuist.dto.SetmealDto;
import com.nuist.pojo.*;
import com.nuist.service.*;
import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    /**
     * 获取套餐分页数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto> > getPage(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageinfo = new Page<>(page, pageSize);
        Page<SetmealDto> pagedto = new Page<>();
        BeanUtils.copyProperties(pageinfo, pagedto, "records");
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.like(null != name, Setmeal::getName, name);
        setmealService.page(pageinfo, qw);
        List<Setmeal> list = pageinfo.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();
        for(Setmeal setmeal : list) {
            SetmealDto temp = new SetmealDto();
            BeanUtils.copyProperties(setmeal, temp);
            temp.setCategoryName(categoryService.getById(temp.getCategoryId()).getName());
            setmealDtos.add(temp);
        }
        pagedto.setRecords(setmealDtos);
        return Result.success(pagedto);
    }

    /**
     * 新建套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveBySetmealDto(setmealDto);
        return Result.success("添加成功");
    }

    /**
     * 根据套餐id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getBySetmealId(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<SetmealDish>();
        qw.eq(SetmealDish::getSetmealId, id);
        setmealDto.setSetmealDishes(setmealDishService.list(qw));
        return Result.success(setmealDto);
    }

    /**
     * 根据套餐id批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public Result<String> deleteById(List<Long> ids) {
        setmealService.removeByList(ids);
        return Result.success("删除成功");
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    @Transactional
    public Result<String> updateBySetmealDto(@RequestBody SetmealDto setmealDto) {
        List<Long> ids = new ArrayList<>();
        ids.add(setmealDto.getId());
        this.deleteById(ids);
        this.saveSetmeal(setmealDto);
        return Result.success("修改成功");
    }


    /**
     * 根据套餐id批量修改套餐停售/在售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable Integer status,
                                       @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> uw = new LambdaUpdateWrapper<>();
        uw.in(Setmeal::getId, ids).set(Setmeal::getStatus, status);
        setmealService.update(uw);
        return Result.success("操作成功");
    }

    /**
     * 根据分类id获取所有在售套餐
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal> > getList(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(null != status, Setmeal::getStatus, status)
                .eq(null != categoryId, Setmeal::getCategoryId, categoryId);
        List<Setmeal> setmealList = setmealService.list(qw);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id获取全部菜品
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishDto> > getAllDishBySetmealId(@PathVariable Long id) {
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(qw);
        List<Dish> dishList;
        if(null == setmealDishList || setmealDishList.size() == 0) {
            //本身就是菜品
            LambdaQueryWrapper<Dish> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(Dish::getId, id);
            dishList = dishService.list(qw1);
        }
        else {
            List<Long> idList = new ArrayList<>();
            for(SetmealDish setmealDish : setmealDishList) {
                idList.add(setmealDish.getDishId());
            }
            LambdaQueryWrapper<Dish> qw1 = new LambdaQueryWrapper<>();
            qw1.in(Dish::getId, idList);
            dishList = dishService.list(qw1);
        }

        List<DishDto> dishDtoList = new ArrayList<>();
        for(Dish dish : dishList) {
            DishDto temp = new DishDto();
            BeanUtils.copyProperties(dish, temp);
            LambdaQueryWrapper<DishFlavor> qw3 = new LambdaQueryWrapper<>();
            qw3.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(qw3);
            temp.setFlavors(dishFlavors);
            dishDtoList.add(temp);
        }
        return Result.success(dishDtoList);
    }
}
