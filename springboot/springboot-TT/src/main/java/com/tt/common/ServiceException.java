package com.tt.common;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final int code;

    public ServiceException(ServiceExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }
}
