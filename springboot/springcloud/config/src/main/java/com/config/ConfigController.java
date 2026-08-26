package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope//刷新配置
public class ConfigController {
    @Value("${db.username}")
    private String configInfo;
    @Value("${redis.username}")
    private String configInfo2;

    @GetMapping("/config")
    public String getConfigInfo() {
        return configInfo;
    }

    @GetMapping("/config2")
    public String getConfigInfo2() {
        return configInfo2;
    }
}