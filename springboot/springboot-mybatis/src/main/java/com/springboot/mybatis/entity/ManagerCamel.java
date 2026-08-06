package com.springboot.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCamel {
    private String mgrId;//与字段mgr_id对应
    private String mgrName;//与字段mgr_name对应
    private String mgrPwd;//与字段mgr_pwd对应
}