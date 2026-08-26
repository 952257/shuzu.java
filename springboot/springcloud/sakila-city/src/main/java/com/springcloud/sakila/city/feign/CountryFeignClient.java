package com.springcloud.sakila.city.feign;

import com.springcloud.sakila.city.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sakila-country", path = "/country")
public interface CountryFeignClient {

    @GetMapping("/{id}")
    CommonResult<Country> queryById(@PathVariable("id") Integer id);

    @PostMapping
    CommonResult<Country> addOne(@RequestBody Country country);
}
