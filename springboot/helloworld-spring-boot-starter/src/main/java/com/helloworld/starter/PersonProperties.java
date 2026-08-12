package com.helloworld.starter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonProperties {
 // 姓名
 private String name;
 // 年龄
 private int age;
 // 性别
 private String sex = "M";
 

} 