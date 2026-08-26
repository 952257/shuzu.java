package com.springcloud.sakila.country.common;

import lombok.Getter;

@Getter
public enum ServiceExceptionEnum {
    SUCCESS(0, "成功"),
    SYS_ERROR(2001001000, "系统正忙，请稍后再试"),
    MISSING_REQUEST_PARAM_ERROR(2001001001, "参数缺失"),
    COUNTRY_NOT_FOUND(1001003000, "国家不存在");

    private final int code;
    private final String message;

    ServiceExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
