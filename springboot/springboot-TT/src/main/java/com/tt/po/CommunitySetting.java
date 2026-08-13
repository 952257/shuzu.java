package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tt_community_setting")
public class CommunitySetting {
    @TableId(type = IdType.INPUT)
    private String settingId;
    private String communityId;
    private String settingGroup;
    private String settingKey;
    private String settingName;
    private String settingValue;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
