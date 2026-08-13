package com.tt.dto;

import lombok.Data;

@Data
public class LoginVo {
    private String userId;
    private String token;
    private String userName;
    private String role;
}
