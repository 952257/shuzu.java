package com.pj.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限/角色 API 演示（基于数据库加载的权限路径）
 */
@RestController
@RequestMapping("/jur/")
public class JurController {

    @RequestMapping("getPermissionList")
    public SaResult getPermissionList() {
        return SaResult.data(StpUtil.getPermissionList());
    }

    @RequestMapping("hasPermission")
    public SaResult hasPermission(String permission) {
        return SaResult.data(StpUtil.hasPermission(permission));
    }

    @RequestMapping("checkPermission")
    public SaResult checkPermission(String permission) {
        StpUtil.checkPermission(permission);
        return SaResult.ok("权限校验通过：" + permission);
    }
}
