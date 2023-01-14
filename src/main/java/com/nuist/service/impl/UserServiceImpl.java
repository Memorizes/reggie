package com.nuist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.UserMapper;
import com.nuist.pojo.User;
import com.nuist.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
