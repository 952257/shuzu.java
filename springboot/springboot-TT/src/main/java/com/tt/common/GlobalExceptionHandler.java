package com.tt.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = "com.tt.controller")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result<Void> serviceExceptionHandler(ServiceException ex, HttpServletResponse response) {
        if (ex.getCode() == 401) {
            response.setStatus(401);
        } else if (ex.getCode() == 403) {
            response.setStatus(403);
        }
        if (ex.getCode() == ServiceExceptionEnum.PARAM_ERROR.getCode()) {
            log.warn("参数错误: {}", ex.getMessage());
        } else {
            log.error("业务异常: {}", ex.getMessage());
        }
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public Result<Void> numberFormatExceptionHandler(NumberFormatException ex) {
        return Result.fail(ServiceExceptionEnum.PARAM_ERROR.getCode(), "数字格式不正确");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> exceptionHandler(Exception e) {
        log.error("系统异常", e);
        return Result.fail(ServiceExceptionEnum.SYS_ERROR.getCode(), ServiceExceptionEnum.SYS_ERROR.getMessage());
    }
}
