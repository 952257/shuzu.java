package com.zhrj.exam.client;

import com.zhrj.exam.config.RemoteAuthProperties;
import com.zhrj.exam.dto.BladeTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 调用第三方 blade-springboot：POST /blade-auth/token
 */
@Component
@RequiredArgsConstructor
public class BladeAuthClient {

    private final RestTemplate restTemplate;
    private final RemoteAuthProperties remoteAuthProperties;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();

    public BladeTokenResponse fetchToken() {
        CacheEntry cached = cache.get("token");
        if (cached != null && cached.expireAt > System.currentTimeMillis()) {
            return cached.response;
        }
        BladeTokenResponse response = requestRemote();
        int expiresIn = response.getData().getExpiresIn() == null ? 3600 : response.getData().getExpiresIn();
        cache.put("token", new CacheEntry(response, System.currentTimeMillis() + (expiresIn - 60) * 1000L));
        return response;
    }

    private BladeTokenResponse requestRemote() {
        RemoteAuthProperties.OAuth oauth = remoteAuthProperties.getOauth();
        // 官方 AuthController 参数名是 grantType、username、password、tenantId
        String url = UriComponentsBuilder.fromHttpUrl(oauth.getUrl())
                .queryParam("tenantId", oauth.getTenantId())
                .queryParam("username", oauth.getUsername())
                .queryParam("password", oauth.getPassword())
                .queryParam("grantType", oauth.getGrantType())
                .queryParam("grant_type", oauth.getGrantType())
                .queryParam("type", oauth.getType())
                .queryParam("scope", oauth.getScope())
                .build(true)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, basicHeader(oauth.getClientId(), oauth.getClientSecret()));
        headers.set("Tenant-Id", oauth.getTenantId());
        ResponseEntity<BladeTokenResponse> entity = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<Void>(headers), BladeTokenResponse.class);
        BladeTokenResponse body = entity.getBody();
        if (body == null || body.getData() == null || body.getData().getAccessToken() == null) {
            throw new IllegalStateException("远程 OAuth 未返回 accessToken，请确认 blade-springboot 已启动");
        }
        return body;
    }

    public static String basicHeader(String clientId, String clientSecret) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
    }

    private static class CacheEntry {
        private final BladeTokenResponse response;
        private final long expireAt;

        private CacheEntry(BladeTokenResponse response, long expireAt) {
            this.response = response;
            this.expireAt = expireAt;
        }
    }
}
