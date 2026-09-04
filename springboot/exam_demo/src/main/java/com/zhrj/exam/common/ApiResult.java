package com.zhrj.exam.common;

import lombok.Data;

@Data
public class ApiResult<T> {

    private int code;
    private boolean success;
    private String msg;
    private T data;

    public static <T> ApiResult<T> ok(T data) {
        ApiResult<T> result = new ApiResult<T>();
        result.setCode(200);
        result.setSuccess(true);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ApiResult<T> fail(String msg) {
        ApiResult<T> result = new ApiResult<T>();
        result.setCode(500);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }
}
