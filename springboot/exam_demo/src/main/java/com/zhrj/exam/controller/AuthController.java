package com.zhrj.exam.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhrj.exam.client.BladeAuthClient;
import com.zhrj.exam.common.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final BladeAuthClient bladeAuthClient;

    public AuthController(BladeAuthClient bladeAuthClient) {
        this.bladeAuthClient = bladeAuthClient;
    }

    @GetMapping("/auth/token")
    public ApiResult<JsonNode> token() {
        return ApiResult.ok(bladeAuthClient.fetchToken());
    }
}
