package com.webclient.client.config;

import io.netty.channel.ChannelOption;
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


    /**
     *  构建连接池配置
     */
    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("custom-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .maxIdleTime(Duration.ofSeconds(10))
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
    public WebClient userWebClient(HttpClient httpClient) {

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .build();
    }

}