package com.springboot.demo.aop.syslog;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    @SysLog
    public String login(String username,String password){
        System.out.println("登录中");
        try {
            Thread.sleep(5000);
            return "登录成功";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
