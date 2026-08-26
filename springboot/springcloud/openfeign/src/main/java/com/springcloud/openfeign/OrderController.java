package com.springcloud.openfeign;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单服务
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderOpenFeignService orderOpenFeignService;

    /**
     * 新增订单
     * @return
     */
    @RequestMapping("/info/{id}")
    public String paymentInfo(@PathVariable Long id){
        System.out.println("使用openfeign查看订单信息");
        //调用扣减
        return orderOpenFeignService.paymentInfo(id);
    }
}