package com.seata.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    @Update("update account set balance = balance - #{amount} where user_id = #{userId}")
    void decreaseBalance(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);
}