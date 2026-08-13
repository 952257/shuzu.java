package com.tt.common;

import lombok.Getter;

@Getter
public enum ServiceExceptionEnum {

    SUCCESS(0, "成功"),
    SYS_ERROR(500, "系统内部错误"),
    PARAM_ERROR(1001, "参数校验失败"),
    UNAUTHORIZED(401, "用户或密码错误"),
    TOKEN_INVALID(401, "登录已过期，请重新登录"),
    STORE_DISABLED(48002, "商户限制登录"),
    USER_NOT_FOUND(1001002000, "用户不存在"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    ROLE_NOT_ALLOW(1003, "仅允许管理员或员工登录");

    private final int code;
    private final String message;

    ServiceExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
