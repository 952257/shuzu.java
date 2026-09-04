package com.pj.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pj.entity.SysUser;
import com.pj.service.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    // 测试登录：http://localhost:8081/user/doLogin?username=aaa&password=123
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password) {
        SysUser user = sysUserService.login(username, password);
        if (user != null) {
            StpUtil.login(user.getId());
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态：http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("当前会话是否登录：" + StpUtil.isLogin());
    }

    // 用户添加：http://localhost:8081/user/addone
    @RequestMapping("addone")
    public SaResult addone() {
        return SaResult.ok("用户添加成功");
    }

    // 用户修改：http://localhost:8081/user/modone
    @RequestMapping("modone")
    public SaResult modone() {
        return SaResult.ok("用户修改成功");
    }

    // 用户查询：http://localhost:8081/user/query
    @RequestMapping("query")
    public SaResult query() {
        return SaResult.ok("用户查询成功");
    }

    // 用户删除：http://localhost:8081/user/delone
    @RequestMapping("delone")
    public SaResult delone() {
        return SaResult.ok("用户删除成功");
    }

    // 用户导出：http://localhost:8081/user/export
    @RequestMapping("export")
    public SaResult export() {
        return SaResult.ok("用户导出成功");
    }
}
