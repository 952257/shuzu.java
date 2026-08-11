package com.springboot.demo.proxy.statics;

public class Test {
    public static void main(String[] args) {
        TargetIn target = new Target();
        TargetIn proxy = new TargetProxy(target);
        proxy.method();
    }
}
