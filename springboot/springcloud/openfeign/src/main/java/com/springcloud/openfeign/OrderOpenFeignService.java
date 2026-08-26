package com.springcloud.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务接口
 * name:指定调用rest接口所对应的服务名
 * path:指定调用rest接口所在的StockController指定的@RequestMapping
 */
@FeignClient(name = "nacos-payment-provider",path = "/payment/nacos")
public interface OrderOpenFeignService {

    //声明需要调用的rest接口对应的方法
    @RequestMapping("/{id}")
    String paymentInfo(@PathVariable("id") Long id);

}