package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("account")
public class Account {
    @TableId(type = IdType.INPUT)
    private String acctId;
    private String acctName;
    private String objId;
    private String objType;
    private String communityId;
    private BigDecimal amount;
    private String acctType;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
