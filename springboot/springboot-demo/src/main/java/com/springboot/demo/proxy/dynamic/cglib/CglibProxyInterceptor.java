package com.springboot.demo.proxy.dynamic.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//不能代理final修饰的类
//目标类是代理类的父类
public class CglibProxyInterceptor implements MethodInterceptor {
    private final Object target; // 目标对象

    public CglibProxyInterceptor(Object target) {
        this.target = target;
    }

    // 代理方法：调用代理对象的任何方法都会执行这里
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 增强逻辑：前置通知
        System.out.println("前置增强");
        // 调用目标对象的原始方法（通过 methodProxy 避免代理对象调用自身导致循环）
        Object result = methodProxy.invokeSuper(proxy, args);
        // 增强逻辑：后置通知
        System.out.println("后置增强");
        return result;
    }
}