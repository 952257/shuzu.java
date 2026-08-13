package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_login")
public class UserLogin {
    @TableId(type = IdType.INPUT)
    private String loginId;
    private String userId;
    private String userName;
    private Date loginTime;
    private String source;
}
