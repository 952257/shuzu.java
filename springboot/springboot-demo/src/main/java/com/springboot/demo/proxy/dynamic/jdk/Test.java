package com.springboot.demo.proxy.dynamic.jdk;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        Target target = new Target();
        // 生成代理对象（需传入类加载器、目标接口、InvocationHandler）
        Target proxy = (Target) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),//类加载器
                target.getClass().getInterfaces(),//目标类实现的接口
                new JdkProxyHandler(target)//动态代理处理器
        );
        proxy.method();
        System.out.println("-----------------------------");
//        Target2In target2 = new Target2();
//        // 生成代理对象（需传入类加载器、目标接口、InvocationHandler）
//        Target2In proxy2 = (Target2In) Proxy.newProxyInstance(
//                target2.getClass().getClassLoader(),//类加载器
//                target2.getClass().getInterfaces(),//目标类实现的接口
//                new JdkProxyHandler(target2)//动态代理处理器
//        );
//        proxy2.method();
    }
}
