package com.zhrj.exam.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhrj.exam.config.RemoteAuthProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class BladeUserClient {

    private final RestTemplate restTemplate;
    private final RemoteAuthProperties properties;
    private final BladeAuthClient bladeAuthClient;
    private final ObjectMapper objectMapper;

    public BladeUserClient(RestTemplate restTemplate,
                           RemoteAuthProperties properties,
                           BladeAuthClient bladeAuthClient,
                           ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.bladeAuthClient = bladeAuthClient;
        this.objectMapper = objectMapper;
    }

    public List<JsonNode> fetchAllUsers(String accessToken) {
        List<JsonNode> users = new ArrayList<JsonNode>();
        int current = 1;
        int size = 10;
        long pages = 1;
        do {
            JsonNode page = fetchPage(accessToken, current, size);
            JsonNode data = page.path("data");
            JsonNode records = data.path("records");
            if (records.isArray()) {
                for (JsonNode record : records) {
                    users.add(record);
                }
            }
            long total = data.path("total").asLong(users.size());
            pages = Math.max(1, (total + size - 1) / size);
            current++;
        } while (current <= pages);
        return users;
    }

    public JsonNode fetchPage(String accessToken, int current, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(properties.getUserListUrl())
                .queryParam("current", current)
                .queryParam("size", size)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, bladeAuthClient.basicHeader());
        headers.set("Blade-Auth", "bearer " + accessToken);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<Void>(headers),
                String.class);
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception ex) {
            throw new IllegalStateException("解析用户台账失败: " + response.getBody(), ex);
        }
    }
}
