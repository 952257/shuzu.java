package com.blademock.controller;

import com.blademock.store.MockDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blade-user")
@RequiredArgsConstructor
public class UserListController {

    private final MockDataStore mockDataStore;

    @GetMapping("/user-list")
    public Map<String, Object> userList(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestHeader(value = "Blade-Auth", required = false) String bladeAuth,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {

        if (!AuthTokenController.validBasic(authorization)) {
            return fail(401, "client 认证失败");
        }
        if (!StringUtils.hasText(bladeAuth) || !mockDataStore.validToken(bladeAuth)) {
            return fail(401, "Blade-Auth 无效或已过期");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("code", 200);
        body.put("success", true);
        body.put("msg", "操作成功");
        body.put("data", mockDataStore.page(current, size));
        return body;
    }

    @PostMapping(value = "/mock/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map<String, Object> add(@RequestParam String account, @RequestParam String name) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", 200);
        body.put("success", true);
        body.put("msg", "已新增模拟用户，再次同步可验证增量");
        body.put("data", mockDataStore.add(account, name));
        return body;
    }

    @PostMapping("/mock/delete/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        MockDataStore.RemoteUser user = mockDataStore.logicalDelete(id);
        if (user == null) {
            return fail(404, "用户不存在");
        }
        Map<String, Object> body = new HashMap<>();
        body.put("code", 200);
        body.put("success", true);
        body.put("msg", "已逻辑删除，再次同步可验证增量删除");
        body.put("data", user);
        return body;
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
