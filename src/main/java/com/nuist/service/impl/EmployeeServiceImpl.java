package com.nuist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.mapper.EmployeeMapper;
import com.nuist.pojo.Employee;
import com.nuist.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
