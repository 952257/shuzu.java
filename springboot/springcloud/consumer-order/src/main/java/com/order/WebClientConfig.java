package com.order;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

	@Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("custom-pool")
                //设置连接池最大连接数为 100，即同时可建立的最大连接数量
                .maxConnections(100)
                //当连接池无可用连接时，请求等待获取连接的超时时间为 5 秒，超时则抛出异常
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                //连接的最大空闲时间为 10 秒，空闲超过该时间的连接会被回收
                .maxIdleTime(Duration.ofSeconds(10))
                //连接的最大存活时间为 30 秒，存活超过该时间的连接会被强制关闭并重建
                .maxLifeTime(Duration.ofSeconds(30))
                .build();
    }

    /**
     * 构建HttpClient连接参数，比如设置超时
     * @param connectionProvider
     * @return
     */
    @Bean
    public HttpClient httpClient(ConnectionProvider connectionProvider) {
        // 配置HttpClient的超时参数
        return HttpClient.create(connectionProvider)
                // 连接超时：建立TCP连接的超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 读取超时：从服务器读取响应的超时时间
                .responseTimeout(Duration.ofSeconds(10));
    }
    @Bean
    @LoadBalanced // 开启负载均衡（支持服务名解析）
    public WebClient.Builder loadBalancedWebClientBuilder(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
    }
    @Bean
    public WebClient paymentWebClient(WebClient.Builder loadBalancedWebClientBuilder,
                                      @Value("${service-url.nacos-payment}") String paymentServiceUrl) {
        return loadBalancedWebClientBuilder
                .baseUrl(paymentServiceUrl)
                .build();
    }

}