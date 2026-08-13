package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tt_purchase")
public class Purchase {
    @TableId(type = IdType.INPUT)
    private String applyId;
    private String communityId;
    private String resourceName;
    private String spec;
    private BigDecimal quantity;
    private BigDecimal price;
    private String applyUser;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
