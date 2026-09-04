package com.stategrid.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BladeTokenResponse {

    private Integer code;
    private Boolean success;
    private String msg;
    private TokenData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenData {
        private String tenantId;
        private String userId;
        private String deptId;
        private String roleId;
        private String account;
        private String userName;
        private String nickName;
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Integer expiresIn;
        private String license;
    }
}
