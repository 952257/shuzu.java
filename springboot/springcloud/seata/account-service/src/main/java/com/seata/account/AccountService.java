package com.seata.account;

import java.math.BigDecimal;

public interface AccountService {

    void decreaseBalance(Integer userId, BigDecimal amount);
}
