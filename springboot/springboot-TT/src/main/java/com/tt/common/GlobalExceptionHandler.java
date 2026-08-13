package com.tt.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.tt.controller")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result<Void> serviceExceptionHandler(ServiceException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> exceptionHandler(Exception e) {
        log.error("系统异常", e);
        return Result.fail(ServiceExceptionEnum.SYS_ERROR.getCode(), ServiceExceptionEnum.SYS_ERROR.getMessage());
    }
}
