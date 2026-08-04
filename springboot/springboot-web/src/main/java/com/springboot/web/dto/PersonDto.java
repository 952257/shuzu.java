package com.springboot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private String name;
    private double height;
    private Integer age;
    private Date birthday;
}
