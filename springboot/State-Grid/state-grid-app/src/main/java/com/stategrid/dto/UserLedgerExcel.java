package com.stategrid.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserLedgerExcel {

    @ExcelProperty("用户ID")
    private Long id;
    @ExcelProperty("租户")
    private String tenantId;
    @ExcelProperty("账号")
    private String account;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("真实姓名")
    private String realName;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("手机")
    private String phone;
    @ExcelProperty("部门ID")
    private String deptId;
    @ExcelProperty("角色ID")
    private String roleId;
    @ExcelProperty("状态")
    private Integer status;
    @ExcelProperty("是否删除")
    private Integer isDeleted;
}
