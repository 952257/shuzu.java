package com.springboot.demo.proxy.dynamic.cglib;

import org.springframework.cglib.proxy.Enhancer;

public class Test {
    public static void main(String[] args) {
        Target target = new Target();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass()); // 设置父类（目标类）
        enhancer.setCallback(new CglibProxyInterceptor(target)); // 设置代理逻辑

        Target proxy = (Target) enhancer.create(); // 创建代理对象
        proxy.method();
    }
}
