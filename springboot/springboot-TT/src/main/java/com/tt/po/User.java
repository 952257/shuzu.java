package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("u_user")
public class User {
    @TableId(type = IdType.INPUT)
    private String userId;
    private String name;
    private String username;
    private String tel;
    private String password;
    private String role;
    @TableLogic
    private String statusCd;
    private Date createTime;
    @TableField(exist = false)
    private String storeId;
}
