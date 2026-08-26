package com.springcloud.sakila.city.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.springcloud.sakila.city.controller")
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<Void> serviceExceptionHandler(ServiceException ex) {
        log.error("出现服务异常", ex);
        CommonResult<Void> commonResult = new CommonResult<>();
        commonResult.setCode(ex.getCode());
        commonResult.setMessage(ex.getMessage());
        return commonResult;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Void> exceptionHandler(Exception e) {
        log.error("出现其他异常", e);
        CommonResult<Void> commonResult = new CommonResult<>();
        commonResult.setCode(ServiceExceptionEnum.SYS_ERROR.getCode());
        commonResult.setMessage(ServiceExceptionEnum.SYS_ERROR.getMessage());
        return commonResult;
    }
}
