package com.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderController {
    @Resource
    private WebClient paymentWebClient;
    @GetMapping(value = "/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Long id) {
       return  paymentWebClient.get()
               .uri("/payment/nacos/"+id).retrieve()
               .bodyToMono(String.class).block();
    }

}