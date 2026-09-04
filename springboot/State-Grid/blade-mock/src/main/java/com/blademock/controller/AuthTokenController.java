package com.blademock.controller;

import com.blademock.store.MockDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blade-auth")
@RequiredArgsConstructor
public class AuthTokenController {

    public static final String EXPECTED_USER = "admin";
    public static final String EXPECTED_PASSWORD_MD5 = "21232f297a57a5a743894a0e4a801fc3";
    public static final String CLIENT_ID = "saber";
    public static final String CLIENT_SECRET = "saber_secret";

    private final MockDataStore mockDataStore;

    @PostMapping("/token")
    public Map<String, Object> token(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "grant_type", required = false) String grantType,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "scope", required = false) String scope) {

        if (!validBasic(authorization)) {
            return fail(401, "client 认证失败，请检查 Authorization Basic");
        }
        if (!EXPECTED_USER.equals(username) || !EXPECTED_PASSWORD_MD5.equalsIgnoreCase(password)) {
            return fail(400, "用户名或密码错误");
        }

        MockDataStore.IssuedToken issued = mockDataStore.issueToken(username);
        Map<String, Object> data = new HashMap<>();
        data.put("tenantId", StringUtils.hasText(tenantId) ? tenantId : "000000");
        data.put("userId", "10001");
        data.put("deptId", "1");
        data.put("roleId", "1");
        data.put("account", username);
        data.put("userName", "管理员");
        data.put("nickName", "管理员");
        data.put("accessToken", issued.getAccessToken());
        data.put("refreshToken", issued.getRefreshToken());
        data.put("tokenType", "bearer");
        data.put("expiresIn", 3600);
        data.put("license", "powered by bladex");
        data.put("grantType", grantType);
        data.put("type", type);
        data.put("scope", scope);

        Map<String, Object> body = new HashMap<>();
        body.put("code", 200);
        body.put("success", true);
        body.put("msg", "操作成功");
        body.put("data", data);
        return body;
    }

    public static boolean validBasic(String authorization) {
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Basic ")) {
            return false;
        }
        try {
            String decoded = new String(Base64.getDecoder().decode(authorization.substring(6)), StandardCharsets.UTF_8);
            return (CLIENT_ID + ":" + CLIENT_SECRET).equals(decoded);
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> fail(int code, String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("success", false);
        body.put("msg", msg);
        body.put("data", null);
        return body;
    }
}
