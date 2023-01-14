package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.common.Result;
import com.nuist.pojo.Category;
import com.nuist.service.CategoryService;
import com.nuist.service.DishService;
import com.nuist.service.SetmealService;
import com.nuist.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 查询分类分页数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Category> > getPage(Integer page, Integer pageSize) {
        Page<Category> pageinfo = new Page(page, pageSize);
        log.info("page = {}, pageSize = {}", page, pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Category::getSort);
        categoryService.page(pageinfo, qw);
        return Result.success(pageinfo);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping("")
    public Result<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success("添加成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping("")
    public Result<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    /**
     * 根据分类id批量删除分类
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public Result<String> deleteCategory(Long ids) {
        categoryService.remove(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据type获取分类数据
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getList(Integer type) {
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(null != type, Category::getType, type);
        List<Category> categories = categoryService.list(qw);
        return Result.success(categories);
    }


}
