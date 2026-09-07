package com.zhrj.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("local_user_ledger")
public class LocalUserLedger {
    @TableId
    private Long id;
    private String tenantId;
    private String account;
    private String name;
    private String realName;
    private String email;
    private String phone;
    private Integer sex;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime lastSyncTime;
}
