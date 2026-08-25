package com.webclient.client.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements Serializable {

    //错误码
    private Integer code = 0;

    //消息
    private String message = "操作成功";

    //数据
    private T data;

}