package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nuist.common.Result;
import com.nuist.pojo.AddressBook;
import com.nuist.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 获取所有地址
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> getList(HttpSession session) {
        Long userId = (Long)session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, userId);
        List<AddressBook> list = addressBookService.list(qw);
        return Result.success(list);
    }

    /**
     * 获取最新更改的地址
     */
    @GetMapping("/lastUpdate")
    public Result<AddressBook> getLastUpdate(HttpSession session) {
        Long userId = (Long)session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, userId);
        qw.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(qw);
        AddressBook lastUpdate = list.get(0);
        return Result.success(lastUpdate);
    }

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<String> addAddress(@RequestBody AddressBook addressBook, HttpSession session) {
        Long userId = (Long)session.getAttribute("user");
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return Result.success("添加成功");
    }


    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return Result.success("修改成功");
    }

    /**
     * 批量删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteAddress(@RequestParam List<Long> ids) {
        addressBookService.removeByIds(ids);
        return Result.success("删除成功");
    }

    /**
     * 查询单个地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result<String> setDefaultAddress(@RequestBody AddressBook addressBook, HttpSession session) {
        Long userId = (Long)session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook defaultAddressBook = addressBookService.getOne(qw);
        if(null != defaultAddressBook) {
            defaultAddressBook.setIsDefault(0);
            addressBookService.updateById(defaultAddressBook);
        }
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success("设置默认地址成功");
    }

    /**
     * 获取默认地址
     * @param session
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress(HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(qw);
        return Result.success(addressBook);
    }

}
