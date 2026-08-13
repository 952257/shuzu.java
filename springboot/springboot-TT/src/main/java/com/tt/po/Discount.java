package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tt_discount")
public class Discount {
    @TableId(type = IdType.INPUT)
    private String discountId;
    private String communityId;
    private String discountName;
    private String discountType;
    private BigDecimal specValue;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
