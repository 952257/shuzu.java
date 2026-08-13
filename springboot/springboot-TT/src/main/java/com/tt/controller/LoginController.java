package com.tt.controller;

import com.tt.common.Result;
import com.tt.dto.LoginDto;
import com.tt.dto.LoginVo;
import com.tt.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
@Slf4j
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login.pcUserLogin")
    public Result<LoginVo> login(@RequestBody LoginDto dto) {
        log.info("员工登录 username={}", dto.getUsername());
        return Result.ok(loginService.login(dto));
    }
}
