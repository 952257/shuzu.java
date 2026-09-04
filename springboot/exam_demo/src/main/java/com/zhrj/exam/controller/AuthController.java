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

    @GetMapping("/token")
    public ApiResult<BladeTokenResponse.TokenData> token() {
        return ApiResult.ok(bladeAuthClient.fetchToken().getData());
    }
}
