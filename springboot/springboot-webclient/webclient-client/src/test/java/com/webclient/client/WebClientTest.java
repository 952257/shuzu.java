package com.webclient.client;

import com.webclient.client.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class WebClientTest {

    @Resource
    private  WebClient userWebClient;

    @Test
    public void test1(){
        Mono<UserDto> userDtoMono = userWebClient.get()
                .uri("/user/{id}", 1)
                .retrieve()
                .bodyToMono(UserDto.class);
        log.info("用户信息：{}", userDtoMono.block());
    }

    @Test
    public void test2(){
        Flux<UserDto> userDtoFlux = userWebClient
                .get()
                .uri("/user")
                .retrieve()
                .bodyToFlux(UserDto.class);
        log.info("用户信息：{}", userDtoFlux.collectList().block());
    }

    @Test
    public void test3(){
        Mono<UserDto> userDtoMono = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/getByEmail")
                        .queryParam("email", "zhangsan@example.com")
                        .build())
                .retrieve()
                .bodyToMono(UserDto.class);
        log.info("用户信息：{}", userDtoMono.block());
    }
}
