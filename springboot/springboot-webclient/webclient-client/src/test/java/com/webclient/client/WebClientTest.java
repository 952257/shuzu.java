package com.webclient.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.webclient.client.common.CommonResult;
import com.webclient.client.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void test4(){
        Mono<CommonResult<UserDto>> userDtoMono = userWebClient.get()
                .uri("/user/{id}", 1)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<UserDto> commonResult = userDtoMono.block();
        log.info("commonResult：{}", commonResult);
    }

    @Test
    public void test5(){
        Mono<CommonResult<List<UserDto>>> Mono = userWebClient
                .get()
                .uri("/user")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){}); //响应数据类型转换
        CommonResult<List<UserDto>> commonResult = Mono.block();
        commonResult.getData().forEach(user -> log.info("User: {}", user.getName()));
    }

    @Test
    public void test6(){
        UserDto newUser = new UserDto(null, "蔡徐坤", "caixukun@example.com");
        Mono<CommonResult<UserDto>> userDtoMono = userWebClient
                .post()
                .uri("/user")
                .bodyValue(newUser) // 自动序列化为 JSON
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<UserDto> commonResult = userDtoMono.block();
        log.info("commonResult：{}", commonResult);
    }

    @Test
    public void test7(){
        UserDto newUser = new UserDto(1L, "易烊千玺", "yiyangqianxi@example.com");
        Mono<CommonResult<UserDto>> userDtoMono = userWebClient.put()
                .uri("/user/{id}", newUser.getId())
                .bodyValue(newUser)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<UserDto> commonResult = userDtoMono.block();


        log.info("commonResult：{}", commonResult);
    }
    @Test
    public void test8(){
        Mono<CommonResult<Void>> commonResultMono = userWebClient
                .delete()
                .uri("/user/{id}", 1)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<Void> commonResult = commonResultMono.block();
        log.info("commonResult：{}", commonResult);
    }

    @Test
    public void test9(){
        String username = Math.random() >= 0.5 ? "张三" : null;
        String email = Math.random() >= 0.5 ? "zhangsan@example.com" : null;
        Mono<CommonResult<Page<UserDto>>> userDtoMono = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/queryForPages")
                        .queryParamIfPresent("username", Optional.ofNullable(username))
                        .queryParamIfPresent("email", Optional.ofNullable(email))
                        .queryParam("pageNum", 1)
                        .queryParam("pageSize", 10)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<Page<UserDto>> commonResult = userDtoMono.block();
        log.info("commonResult：{}", commonResult);
        commonResult.getData().getRecords().forEach(user -> log.info("User: {}", user.getName()));
    }

    @Test
    public void test10(){
        Mono<CommonResult<UserDto>> userDtoMono = userWebClient.get()
                .uri("/users/{id}", 1)//404
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new RuntimeException("客户端错误：" + response.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(new RuntimeException("服务器错误：" + response.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<>(){});
        CommonResult<UserDto> commonResult = userDtoMono.block();
        log.info("commonResult：{}", commonResult);
    }

    @Test
    public void test13() throws InterruptedException {
        Mono<CommonResult<UserDto>> userDtoMono = userWebClient.get()
                .uri("/user/subscribe")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});

        userDtoMono.subscribe(
                result -> log.info("订阅结果：{}", result),
                error -> {
                    log.error("订阅错误：{}", error.getMessage());
                });
        TimeUnit.SECONDS.sleep(10);
    }
}
