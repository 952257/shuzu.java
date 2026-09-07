package com.zhrj.exam.common;

import lombok.Data;

@Data
public class ApiResult<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResult<T> ok(T data) {
        ApiResult<T> result = new ApiResult<T>();
        result.setSuccess(true);
        result.setMessage("ok");
        result.setData(data);
        return result;
    }

    public static <T> ApiResult<T> fail(String message) {
        ApiResult<T> result = new ApiResult<T>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
