package com.nuist.common;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String msg = ex.getMessage();
        log.info(msg);
        String[] msgs = msg.split(" ");
        for(String mmsg : msgs) System.out.println(mmsg);
        if("Duplicate".equals(msgs[0]) && "entry".equals(msgs[1])) {
            return Result.fail("用户名" + msgs[2] + "已存在");
        }
        return Result.fail("添加失败");
    }

    @ExceptionHandler(CustomerException.class)
    public Result<String> customerExceptionHandler(CustomerException ex) {
        return Result.fail(ex.getMessage());
    }

}
