package com.springcloud.sakila.country.common;

import lombok.Getter;

@Getter
public final class ServiceException extends RuntimeException {
    private final Integer code;

    public ServiceException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
        this.code = serviceExceptionEnum.getCode();
    }
}
