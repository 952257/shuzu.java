package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("store_user")
public class StoreUser {
    @TableId(type = IdType.INPUT)
    private String storeUserId;
    private String storeId;
    private String userId;
    private String relCd;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
