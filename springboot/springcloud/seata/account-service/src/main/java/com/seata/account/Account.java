package com.seata.account;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Account {
    @TableId
    private Long id;
    private String accountId;
    private Integer userId;
    private BigDecimal balance;
    private String status;
}