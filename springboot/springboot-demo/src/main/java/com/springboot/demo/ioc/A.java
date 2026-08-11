package com.springboot.demo.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//低耦合 高内聚
@Component//组件 默认是单例
//@Scope("singleton")
public class A {

//    @Resource
    @Autowired//默认按照类型来装配
    @Qualifier("b")
    private BIn bIn;

    public void aaa(){
        System.out.println("aaa");
        bIn.bbb();
    }
}
