package com.nuist.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nuist.common.Result;
import com.nuist.dto.UserDto;
import com.nuist.pojo.User;
import com.nuist.service.UserService;
import com.nuist.utils.EmailUtils;
import com.nuist.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpServletRequest request) {
        String email = user.getEmail();
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        EmailUtils.sendEMail(email, code);
        request.getSession().setAttribute(email, code);
        return Result.success("验证码发送成功");
    }

    /**
     * 登录验证
     * @param userDto
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<String> logInCheck(@RequestBody UserDto userDto, HttpServletRequest request) {
        String email = userDto.getEmail();
        String code = userDto.getCode();
        String codeInSession = (String)request.getSession().getAttribute(email);
        if(code.equals(codeInSession)) {
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.eq(User::getEmail, email);
            User selectUser = userService.getOne(qw);
            Long userId;
            if(selectUser == null) {
                userService.save(userDto);
                userId = userDto.getId();
            }
            else userId = selectUser.getId();
            request.getSession().setAttribute("user", userId);
            return Result.success("登录成功");
        }
        return Result.fail("登录失败");
        //request.getSession().setAttribute("user", 1609773562871169026L);
        //return Result.success("登录成功");
    }
}
