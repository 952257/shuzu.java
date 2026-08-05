package com.springboot.sakila.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements Serializable {

    //错误码
    private int code;

    //消息
    private String message = "操作成功";

    //数据
    private T data;

}