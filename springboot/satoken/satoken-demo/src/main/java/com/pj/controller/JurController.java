package com.pj.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限认证、角色校验（教程做到角色校验）
 */
@RestController
@RequestMapping("/jur/")
public class JurController {

    // ---------- 权限校验 ----------

    // 获取当前账号权限集合  ---- http://localhost:8081/jur/getPermissionList
    @RequestMapping("getPermissionList")
    public SaResult getPermissionList() {
        return SaResult.data(StpUtil.getPermissionList());
    }

    // 判断是否含有指定权限  ---- http://localhost:8081/jur/hasPermission?permission=user.add
    @RequestMapping("hasPermission")
    public SaResult hasPermission(String permission) {
        return SaResult.data(StpUtil.hasPermission(permission));
    }

    // 校验指定权限，未通过抛 NotPermissionException  ---- http://localhost:8081/jur/checkPermission?permission=user.add
    @RequestMapping("checkPermission")
    public SaResult checkPermission(String permission) {
        StpUtil.checkPermission(permission);
        return SaResult.ok("权限校验通过：" + permission);
    }

    // 必须全部通过  ---- http://localhost:8081/jur/checkPermissionAnd
    @RequestMapping("checkPermissionAnd")
    public SaResult checkPermissionAnd() {
        StpUtil.checkPermissionAnd("user.add", "user.delete", "user.get");
        return SaResult.ok("权限 AND 校验通过");
    }

    // 其一通过即可  ---- http://localhost:8081/jur/checkPermissionOr
    @RequestMapping("checkPermissionOr")
    public SaResult checkPermissionOr() {
        StpUtil.checkPermissionOr("user.add", "user.delete", "user.get");
        return SaResult.ok("权限 OR 校验通过");
    }

    // ---------- 角色校验 ----------

    // 获取当前账号角色集合  ---- http://localhost:8081/jur/getRoleList
    @RequestMapping("getRoleList")
    public SaResult getRoleList() {
        return SaResult.data(StpUtil.getRoleList());
    }

    // 判断是否拥有指定角色  ---- http://localhost:8081/jur/hasRole?role=super-admin
    @RequestMapping("hasRole")
    public SaResult hasRole(String role) {
        return SaResult.data(StpUtil.hasRole(role));
    }

    // 校验指定角色，未通过抛 NotRoleException  ---- http://localhost:8081/jur/checkRole?role=super-admin
    @RequestMapping("checkRole")
    public SaResult checkRole(String role) {
        StpUtil.checkRole(role);
        return SaResult.ok("角色校验通过：" + role);
    }

    // 必须全部通过  ---- http://localhost:8081/jur/checkRoleAnd
    @RequestMapping("checkRoleAnd")
    public SaResult checkRoleAnd() {
        StpUtil.checkRoleAnd("super-admin", "shop-admin");
        return SaResult.ok("角色 AND 校验通过");
    }

    // 其一通过即可  ---- http://localhost:8081/jur/checkRoleOr
    @RequestMapping("checkRoleOr")
    public SaResult checkRoleOr() {
        StpUtil.checkRoleOr("super-admin", "shop-admin");
        return SaResult.ok("角色 OR 校验通过");
    }

}
