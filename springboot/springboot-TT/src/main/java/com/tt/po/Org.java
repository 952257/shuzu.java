package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tt_org")
public class Org {
    @TableId(type = IdType.INPUT)
    private String orgId;
    private String orgName;
    private String parentId;
    private String orgLevel;
    private String communityId;
    private String description;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
