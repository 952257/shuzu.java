package com.seata.order;

import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
@Slf4j
public class AccountClient {

    @Resource
    private WebClient accountWebClient;
    public String decreaseBalance(Integer userId, BigDecimal amount) {
        log.info("用户 {} 扣减金额 {}", userId, amount);
        Mono<String> userDtoMono = accountWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/decrease")
                        .queryParam("userId", userId)
                        .queryParam("amount", amount)
                        .build())
                //使用WebClient必须手动传递全局事务ID
                .header(RootContext.KEY_XID,RootContext.getXID())
                .retrieve()
                .bodyToMono(String.class);
        String result = userDtoMono.block();
        return result;
    }
}
