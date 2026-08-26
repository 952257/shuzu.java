package com.springcloud.sentinel;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单服务
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 新增订单
     * @return
     */
    @RequestMapping("/addOrder")
    public String addOrder(){
        System.out.println("订单新增成功");

        return "订单服务-订单新增成功";
    }

    @RequestMapping("/test/{id}")
    public String testSlow(@PathVariable Integer id) throws InterruptedException {
        System.out.println("test");
        if(2 == id){
            Thread.sleep(5000);
        }
        return "success";
    }
}