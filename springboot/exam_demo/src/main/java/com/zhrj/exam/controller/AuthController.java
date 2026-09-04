package com.zhrj.exam.controller;

import com.zhrj.exam.client.BladeAuthClient;
import com.zhrj.exam.common.ApiResult;
import com.zhrj.exam.dto.BladeTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BladeAuthClient bladeAuthClient;

    /**
     * 依据 yml 中的账户信息调用远程 OAuth，返回 Token。
     */
    @GetMapping("/token")
    public ApiResult<BladeTokenResponse.TokenData> token() {
        BladeTokenResponse response = bladeAuthClient.fetchToken();
        return ApiResult.ok(response.getData());
    }
}
