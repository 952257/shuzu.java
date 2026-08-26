package com.springcloud.sakila.country;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SakilaSpringCloudCountryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SakilaSpringCloudCountryApplication.class, args);
    }
}
