package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("account_detail")
public class AccountDetail {
    @TableId(type = IdType.INPUT)
    private String detailId;
    private String acctId;
    private String detailType;
    private BigDecimal amount;
    private String remark;
    private String state;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
