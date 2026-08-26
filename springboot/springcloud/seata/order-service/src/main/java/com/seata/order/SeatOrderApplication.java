package com.seata.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeatOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeatOrderApplication.class, args);
    }
}