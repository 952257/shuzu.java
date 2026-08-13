package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(type = IdType.INPUT)
    private String complaintId;
    private String communityId;
    private String typeCd;
    private String complaintName;
    private String tel;
    private String roomId;
    private String context;
    private String state;
    private String currentUserId;
    private String currentUserName;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
