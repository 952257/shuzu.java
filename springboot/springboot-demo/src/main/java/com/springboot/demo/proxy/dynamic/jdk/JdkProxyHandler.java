package com.springboot.demo.proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//动态处理器 生成代理对象的工具
//JDK动态代理 只能代理必须实现某个接口的类
//代理对象和目标对象是兄弟关系 实现了同一个接口
public class JdkProxyHandler implements InvocationHandler {
    private final Object target; // 目标对象

    public JdkProxyHandler(Object target) {
        this.target = target;
    }

    // 代理方法：调用代理对象的任何方法都会执行这里
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 增强逻辑：前置通知
        System.out.println("前置增强");
        // 调用目标对象的原始方法
        Object result = method.invoke(target, args);
        // 增强逻辑：后置通知
        System.out.println("后置增强");
        return result;
    }
}