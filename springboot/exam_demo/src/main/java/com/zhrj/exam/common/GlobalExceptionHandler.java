package com.zhrj.exam.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handle(Exception e) {
        log.error("接口异常", e);
        return ApiResult.fail(e.getMessage());
    }
}
