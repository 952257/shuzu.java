package com.seata.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;
    @PostMapping("/decrease")
    public String decrease(Integer userId, BigDecimal amount) {
        log.info("用户 {} 扣减金额 {}", userId, amount);
        accountService.decreaseBalance(userId, amount);
        return "success";
    }
}