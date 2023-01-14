package com.nuist.common;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> success(T data) {
        Result<T> res = new Result<>();
        res.setData(data);
        res.setCode(1);
        return res;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> res = new Result<>();
        res.setCode(0);
        res.setMsg(msg);
        return res;
    }
}
