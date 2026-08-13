package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tt_inspection")
public class Inspection {
    @TableId(type = IdType.INPUT)
    private String taskId;
    private String communityId;
    private String planName;
    private String pointName;
    private String staffName;
    private Date inspectTime;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
