package com.example.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private String status;
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus("success");
        result.setCode(200);
        result.setMessage("请求成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setStatus("error");
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 