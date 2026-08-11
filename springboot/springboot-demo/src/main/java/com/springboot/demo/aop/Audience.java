package com.springboot.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 观众
 */
@Component
@Aspect
public class Audience {

    //切点 定义了切哪里
    @Pointcut("execution(* com.springboot.demo.aop.core.*.*())")
    public void pc(){

    }

    @Before("pc()")
    public void takeSeat(){
        System.out.println("观众坐下");
    }

    @Before("pc()")
    public void turnOffPhone(){
        System.out.println("观众关掉手机");
    }

    @AfterReturning("pc()")
    public void applaud(){
        System.out.println("观众鼓掌");
    }

    @AfterThrowing("pc()")
    public void throwEggs(){
        System.out.println("观众扔鸡蛋");
    }

    @Around("pc()")
    public Object aaa(ProceedingJoinPoint joinPoint){
        System.out.println("前置切入");
        try {
            Object result = joinPoint.proceed();
            System.out.println("后置切入");
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
