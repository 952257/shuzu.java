package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pay_fee_config")
public class FeeConfig {
    @TableId(type = IdType.INPUT)
    private String configId;
    private String communityId;
    private String feeTypeCd;
    private String feeName;
    private String feeFlag;
    private String computingFormula;
    private BigDecimal squarePrice;
    private BigDecimal additionalAmount;
    private String billType;
    private String paymentCycle;
    private Date startTime;
    private Date endTime;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
