package com.springboot.demo.aop.syslog;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SyslogAspect {

//    @Pointcut("execution(* com.springboot.demo.aop.syslog.service.*.*(..))")
    @Pointcut("@annotation(com.springboot.demo.aop.syslog.SysLog)")
    public void pc(){

    }

    @Before("pc()")
    public void printParams(JoinPoint joinPoint){
        Class<?> aClass = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("进入类：{}，方法：{}，参数：{}",aClass,methodName,args);
    }

    @Around("pc()")
    public Object printTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("方法耗时：{}毫秒",end-start);
        return result;
    }

    @AfterReturning(pointcut = "pc()",returning = "returnValue")
    public void printReturn(Object returnValue){
        log.info("返回结果：{}",returnValue);
    }
}
