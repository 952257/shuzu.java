package com.zhrj.exam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BladeUserPageResponse {

    private Integer code;
    private Boolean success;
    private String msg;
    private PageData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageData {
        private List<RemoteUser> records;
        private Long total;
        private Long size;
        private Long current;
        private Long pages;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RemoteUser {
        private Long id;
        private String tenantId;
        private String account;
        private String name;
        private String realName;
        private String email;
        private String phone;
        private String roleId;
        private String deptId;
        private Integer status;
        private Integer isDeleted;
        private String createTime;
        private String updateTime;
    }
}
