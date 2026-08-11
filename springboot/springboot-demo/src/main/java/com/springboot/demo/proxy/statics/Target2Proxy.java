package com.springboot.demo.proxy.statics;

public class Target2Proxy implements Target2In{

    private TargetIn target;

    public Target2Proxy(TargetIn target){
        this.target = target;
    }

    @Override
    public void method() {
        System.out.println(66666);
        target.method();
        System.out.println(66666);
    }
}
