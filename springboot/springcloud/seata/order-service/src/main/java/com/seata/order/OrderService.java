package com.seata.order;

import java.math.BigDecimal;

public interface OrderService {

    void createOrder(Integer userId, BigDecimal amount, boolean rollback);
}
