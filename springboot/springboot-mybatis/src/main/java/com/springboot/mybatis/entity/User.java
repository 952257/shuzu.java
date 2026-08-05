package com.springboot.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data//提供类的get、set、equals、hashCode、canEqual、toString方法
@AllArgsConstructor //提供类的全参构造器
@NoArgsConstructor //提供类的无参构造器
public class User {
    private Integer id;
    private String name;
    private String password;
    private String sex;
    private Date birthday;
    private Date registTime;
}