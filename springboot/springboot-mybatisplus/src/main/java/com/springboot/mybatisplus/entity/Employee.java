package com.springboot.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employee")
public class Employee {
    @TableId
    private Long empId;
//    @TableField("emp_name")
    private String name;
    private String empGender;
    private Integer age;
    private String email;

    @TableField(exist = false)
    private String remark;
}