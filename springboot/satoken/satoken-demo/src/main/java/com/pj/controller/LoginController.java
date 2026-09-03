package com.pj.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.entity.SysUser;
import com.pj.service.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录测试
 */
@RestController
@RequestMapping("/acc/")
public class LoginController {

    private final SysUserService sysUserService;

    public LoginController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    // 测试登录  ---- http://localhost:8081/acc/doLogin?name=aaa&pwd=123
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        SysUser user = sysUserService.login(name, pwd);
        if (user != null) {
            StpUtil.login(user.getId());
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态  ---- http://localhost:8081/acc/isLogin
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 查询当前账号权限  ---- http://localhost:8081/acc/permissions
    @RequestMapping("permissions")
    public SaResult permissions() {
        return SaResult.data(StpUtil.getPermissionList());
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
