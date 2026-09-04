package com.stategrid.client;

import com.stategrid.config.RemoteAuthProperties;
import com.stategrid.dto.BladeUserPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        headers.set("Blade-Auth", accessToken);
        headers.set("Tenant-Id", oauth.getTenantId());
        ResponseEntity<BladeUserPageResponse> entity = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), BladeUserPageResponse.class);
        BladeUserPageResponse body = entity.getBody();
        if (body == null || body.getData() == null) {
            throw new IllegalStateException("用户台账接口未返回数据");
        }
        return body;
    }
}
