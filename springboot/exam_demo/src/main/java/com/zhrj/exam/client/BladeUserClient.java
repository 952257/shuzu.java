package com.zhrj.exam.client;

import com.zhrj.exam.config.RemoteAuthProperties;
import com.zhrj.exam.dto.BladeUserPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 调用第三方 blade-springboot：GET /blade-user/user-list
 */
@Component
@RequiredArgsConstructor
public class BladeUserClient {

    private final RestTemplate restTemplate;
    private final RemoteAuthProperties remoteAuthProperties;

    public BladeUserPageResponse fetchUsers(String accessToken, long current, long size) {
        RemoteAuthProperties.OAuth oauth = remoteAuthProperties.getOauth();
        String url = UriComponentsBuilder.fromHttpUrl(remoteAuthProperties.getUser().getUrl())
                .queryParam("current", current)
                .queryParam("size", size)
                .build(true)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        headers.set(HttpHeaders.AUTHORIZATION, BladeAuthClient.basicHeader(oauth.getClientId(), oauth.getClientSecret()));
        // Blade Secure 只解析 "bearer " / "crypto " 前缀，裸 JWT 会返回 401 请求未授权
        headers.set("Blade-Auth", toBladeAuth(accessToken));
        headers.set("Tenant-Id", oauth.getTenantId());
        ResponseEntity<BladeUserPageResponse> entity = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(headers), BladeUserPageResponse.class);
        BladeUserPageResponse body = entity.getBody();
        if (body == null || body.getData() == null || Boolean.FALSE.equals(body.getSuccess())) {
            String msg = body == null ? "空响应" : body.getMsg();
            throw new IllegalStateException("用户台账接口未返回数据: " + msg);
        }
        return body;
    }

    static String toBladeAuth(String accessToken) {
        if (accessToken == null) {
            return null;
        }
        String trimmed = accessToken.trim();
        if (trimmed.regionMatches(true, 0, "bearer ", 0, 7)
                || trimmed.regionMatches(true, 0, "crypto ", 0, 7)) {
            return trimmed;
        }
        return "bearer " + trimmed;
    }
}
