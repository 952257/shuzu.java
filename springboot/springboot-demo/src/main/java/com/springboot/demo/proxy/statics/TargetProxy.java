package com.springboot.demo.proxy.statics;

public class TargetProxy implements TargetIn{

    private TargetIn target;

    public TargetProxy(TargetIn target){
        this.target = target;
    }

    @Override
    public void method() {
        System.out.println(66666);
        target.method();
        System.out.println(66666);
    }
}
