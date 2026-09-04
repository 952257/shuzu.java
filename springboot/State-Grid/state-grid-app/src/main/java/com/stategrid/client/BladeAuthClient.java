package com.stategrid.client;

import com.stategrid.config.RemoteAuthProperties;
import com.stategrid.dto.BladeTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class BladeAuthClient {

    private static final String TOKEN_CACHE_KEY = "state-grid:oauth:token";

    private final RestTemplate restTemplate;
    private final RemoteAuthProperties remoteAuthProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public BladeTokenResponse fetchToken() {
        String cached = readCache();
        if (cached != null) {
            try {
                return objectMapper.readValue(cached, BladeTokenResponse.class);
            } catch (Exception e) {
                log.warn("解析缓存 Token 失败，将重新请求");
            }
        }
        BladeTokenResponse response = requestRemote();
        writeCache(response);
        return response;
    }

    public BladeTokenResponse fetchTokenFresh() {
        BladeTokenResponse response = requestRemote();
        writeCache(response);
        return response;
    }

    private BladeTokenResponse requestRemote() {
        RemoteAuthProperties.OAuth oauth = remoteAuthProperties.getOauth();
        String url = UriComponentsBuilder.fromHttpUrl(oauth.getUrl())
                .queryParam("tenantId", oauth.getTenantId())
                .queryParam("username", oauth.getUsername())
                .queryParam("password", oauth.getPassword())
                .queryParam("grant_type", oauth.getGrantType())
                .queryParam("type", oauth.getType())
                .queryParam("scope", oauth.getScope())
                .build(true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, basicHeader(oauth.getClientId(), oauth.getClientSecret()));

        ResponseEntity<BladeTokenResponse> entity = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(headers), BladeTokenResponse.class);
        BladeTokenResponse body = entity.getBody();
        if (body == null || body.getData() == null || body.getData().getAccessToken() == null) {
            throw new IllegalStateException("远程 OAuth 未返回 accessToken");
        }
        return body;
    }

    public static String basicHeader(String clientId, String clientSecret) {
        String raw = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private String readCache() {
        try {
            return stringRedisTemplate.opsForValue().get(TOKEN_CACHE_KEY);
        } catch (Exception e) {
            log.warn("Redis 不可用，跳过 Token 缓存: {}", e.getMessage());
            return null;
        }
    }

    private void writeCache(BladeTokenResponse response) {
        try {
            int expiresIn = response.getData().getExpiresIn() == null ? 3600 : response.getData().getExpiresIn();
            int ttl = Math.max(expiresIn - 60, 60);
            stringRedisTemplate.opsForValue().set(
                    TOKEN_CACHE_KEY,
                    objectMapper.writeValueAsString(response),
                    Duration.ofSeconds(ttl));
        } catch (Exception e) {
            log.warn("写入 Token 缓存失败: {}", e.getMessage());
        }
    }
}
