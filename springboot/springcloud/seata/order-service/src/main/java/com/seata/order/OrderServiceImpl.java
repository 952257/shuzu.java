package com.seata.order;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AccountClient accountClient;

    @GlobalTransactional // ← 关键注解：开启全局事务
    public void createOrder(Integer userId, BigDecimal amount, boolean rollback) {
        log.info("开始创建订单，全局事务 ID: {}", RootContext.getXID());
        String xid = RootContext.getXID();
        System.out.println("第1个子事务XID：" + xid); // 必须非空，
        // 1. 创建订单
        Order order = new Order()
                .setOrderId("ORD-" + System.currentTimeMillis())
                .setUserId(userId)
                .setAmount(amount)
                .setStatus("CREATED");

        orderMapper.insert(order);

        // 2. 扣减余额（远程调用）
        String result = accountClient.decreaseBalance(userId, amount);
        if (!"success".equals(result)) {
            throw new RuntimeException("扣减余额失败");
        }

        if (rollback) {
            throw new RuntimeException("模拟异常，测试全局回滚");
        }

        log.info("订单创建完成");
    }
}