package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.common.Result;
import com.nuist.pojo.Employee;
import com.nuist.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 登录验证
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.eq(Employee::getUsername, employee.getUsername());
        Employee selectEmployee = employeeService.getOne(qw);

        if (null == selectEmployee) {
            return Result.fail("该用户名不存在");
        }
        if(!password.equals(selectEmployee.getPassword())) {
            return Result.fail("用户名或密码错误");
        }
        if(selectEmployee.getStatus() == 0) {
            return Result.fail("该账号已被锁定");
        }
        request.getSession().setAttribute("employee", selectEmployee.getId());
        return Result.success(selectEmployee);
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工用户
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("")
    public Result addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        employeeService.save(employee);
        return Result.success("添加成功");
    }

    /**
     * 查询员工分页数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Employee>> getPage(Integer page, Integer pageSize, String name) {
        Page<Employee> pageinfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.like(null != name, Employee::getName, name);
        employeeService.page(pageinfo, qw);
        return Result.success(pageinfo);
    }

    /**
     * 根据员工id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getOne(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping("")
    public Result<String> updateOne(HttpServletRequest request, @RequestBody Employee employee) {
        Long id = (Long)request.getSession().getAttribute("employee");
        LambdaUpdateWrapper<Employee> uw = new LambdaUpdateWrapper<>();
        uw.eq(Employee::getId, employee.getId());
        employeeService.update(employee, uw);
        return Result.success("修改成功");
    }
}
