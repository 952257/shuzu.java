package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tt_vote")
public class Vote {
    @TableId(type = IdType.INPUT)
    private String voteId;
    private String communityId;
    private String title;
    private String voteType;
    private String context;
    private Date startTime;
    private Date endTime;
    private String state;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
