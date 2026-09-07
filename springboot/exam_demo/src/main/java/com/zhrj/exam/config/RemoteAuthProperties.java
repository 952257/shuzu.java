package com.zhrj.exam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "remote.auth")
public class RemoteAuthProperties {
    private String tokenUrl;
    private String userListUrl;
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String username;
    private String password;
    private String grantType;
    private String type;
    private String scope;
}
