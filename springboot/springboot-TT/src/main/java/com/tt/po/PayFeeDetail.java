package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pay_fee_detail")
public class PayFeeDetail {
    @TableId(type = IdType.INPUT)
    private String detailId;
    private String feeId;
    private String communityId;
    private BigDecimal cycles;
    private BigDecimal receivableAmount;
    private BigDecimal receivedAmount;
    private Date payTime;
    private Date startTime;
    private Date endTime;
    private String state;
    private String auditState;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
