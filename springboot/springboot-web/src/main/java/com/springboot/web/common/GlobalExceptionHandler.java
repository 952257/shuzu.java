package com.springboot.web.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理 ServiceException 异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<Void> serviceExceptionHandler(ServiceException ex) {
    	log.error("出现服务异常",ex);
        // 包装 CommonResult 结果
        CommonResult<Void> commonResult = new CommonResult<>();
        commonResult.setCode(ex.getCode());
        commonResult.setMessage(ex.getMessage());
        return commonResult;
    }

    /**
     * 处理其它 Exception 异常
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Void> exceptionHandler(Exception e) {
        log.error("出现其他异常", e);
        CommonResult<Void> commonResult = new CommonResult<>();
        commonResult.setCode(ServiceExceptionEnum.SYS_ERROR.getCode());
        commonResult.setMessage(ServiceExceptionEnum.SYS_ERROR.getMessage());
        // 返回 ERROR CommonResult
        return commonResult;
    }

}