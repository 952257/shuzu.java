package com.springboot.demo.proxy.statics;

public class Target implements TargetIn{

    public void method(){
        System.out.println("目标对象的方法");
    }
}
