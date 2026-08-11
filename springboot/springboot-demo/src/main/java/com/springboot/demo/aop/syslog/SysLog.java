package com.springboot.demo.aop.syslog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface SysLog {
}
