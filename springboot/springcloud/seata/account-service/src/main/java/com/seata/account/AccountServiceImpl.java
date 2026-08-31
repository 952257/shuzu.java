package com.seata.account;

import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountMapper accountMapper;
    @Override
    @Transactional//此处不需要添加 `@GlobalTransactional` 不需要（只有发起者需要）
    public void decreaseBalance(Integer userId, BigDecimal amount) {
        String xid = RootContext.getXID();
        System.out.println("第二个子事务XID：" + xid); // 必须非空，
        accountMapper.decreaseBalance(userId, amount);
    }
}