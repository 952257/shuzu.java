package com.zhrj.exam.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhrj.exam.config.RemoteAuthProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BladeAuthClient {

    private final RestTemplate restTemplate;
    private final RemoteAuthProperties properties;
    private final ObjectMapper objectMapper;

    public BladeAuthClient(RestTemplate restTemplate, RemoteAuthProperties properties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public JsonNode fetchToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, basicHeader());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("tenantId", properties.getTenantId());
        form.add("username", properties.getUsername());
        form.add("password", properties.getPassword());
        form.add("grant_type", properties.getGrantType());
        form.add("grantType", properties.getGrantType());
        form.add("type", properties.getType());
        form.add("scope", properties.getScope());

        ResponseEntity<String> response = restTemplate.postForEntity(
                properties.getTokenUrl(),
                new HttpEntity<MultiValueMap<String, String>>(form, headers),
                String.class);
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception ex) {
            throw new IllegalStateException("解析远程 Token 失败: " + response.getBody(), ex);
        }
    }

    public String fetchAccessToken() {
        JsonNode root = fetchToken();
        JsonNode data = root.path("data");
        String accessToken = firstText(data, "accessToken", "access_token");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalStateException("远程 Token 响应中没有 accessToken: " + root);
        }
        return accessToken;
    }

    public String basicHeader() {
        String raw = properties.getClientId() + ":" + properties.getClientSecret();
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private String firstText(JsonNode node, String... names) {
        for (String name : names) {
            JsonNode value = node.path(name);
            if (!value.isMissingNode() && !value.isNull() && value.asText().length() > 0) {
                return value.asText();
            }
        }
        return null;
    }
}
