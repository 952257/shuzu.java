package com.seata.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order/create")
    public String create(@RequestParam Integer userId, @RequestParam BigDecimal amount) {
        try {
            orderService.createOrder(userId, amount);
            return "订单创建成功";
        } catch (Exception e) {
            log.error("订单创建失败", e);
            return "失败：" + e.getMessage();
        }
    }
}