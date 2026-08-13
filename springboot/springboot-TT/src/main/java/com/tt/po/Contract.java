package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tt_contract")
public class Contract {
    @TableId(type = IdType.INPUT)
    private String contractId;
    private String communityId;
    private String contractCode;
    private String contractName;
    private String contractType;
    private String partyA;
    private String partyB;
    private BigDecimal amount;
    private Date startTime;
    private Date endTime;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
