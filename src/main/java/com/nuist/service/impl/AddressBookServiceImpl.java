package com.nuist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.AddressBookMapper;
import com.nuist.pojo.AddressBook;
import com.nuist.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
