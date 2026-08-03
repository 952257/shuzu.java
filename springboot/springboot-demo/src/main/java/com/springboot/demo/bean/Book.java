package com.springboot.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component//注册到Spring当中
@Data//setter getter toString hashcode equals
@AllArgsConstructor//所有参数的构造器
@NoArgsConstructor//无参构造器
public class Book {

    @Value("${book.name}")
    private String name;

    @Value("${book.author}")
    private String author;

}