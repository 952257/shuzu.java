package com.zhrj.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("local_user_ledger")
public class LocalUserLedger {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String tenantId;
    private String account;
    private String name;
    private String realName;
    private String email;
    private String phone;
    private String roleId;
    private String deptId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime syncTime;
}
