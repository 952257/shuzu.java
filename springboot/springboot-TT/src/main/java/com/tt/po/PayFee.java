package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pay_fee")
public class PayFee {
    @TableId(type = IdType.INPUT)
    private String feeId;
    private String configId;
    private String communityId;
    private String payerObjId;
    private String payerObjType;
    private String feeName;
    private BigDecimal amount;
    private String state;
    private Date startTime;
    private Date endTime;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
