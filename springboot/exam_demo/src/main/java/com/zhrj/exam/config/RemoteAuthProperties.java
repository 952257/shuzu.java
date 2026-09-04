package com.zhrj.exam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "remote")
public class RemoteAuthProperties {

    private OAuth oauth = new OAuth();
    private User user = new User();

    @Data
    public static class OAuth {
        private String url;
        private String clientId;
        private String clientSecret;
        private String tenantId;
        private String username;
        private String password;
        private String grantType;
        private String type;
        private String scope;
    }

    @Data
    public static class User {
        private String url;
        private int pageSize = 10;
    }
}
