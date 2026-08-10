package com.springboot.mybatis.service.impl;

import com.springboot.mybatis.entity.UserMyBatis;
import com.springboot.mybatis.mapper.UserMyBatisMapper;
import com.springboot.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
@CacheConfig(cacheNames="userMybatis")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMyBatisMapper userMyBatisMapper;

    @Resource
    private UserService userService;

    private final TransactionTemplate transactionTemplate;

    @Autowired
    public UserServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }



    @Override
    public void addTwo(UserMyBatis userMyBatis1, UserMyBatis userMyBatis2) {
        log.info("是不是同一个自己:{}", this == userService);
        userService.addTwo2(userMyBatis1,userMyBatis2);
    }


    @Override
    public void addOne(UserMyBatis userMyBatis){
        userMyBatisMapper.insert(userMyBatis.getName(), userMyBatis.getAge());
    }

    @Transactional(isolation= Isolation.READ_COMMITTED,rollbackFor = RuntimeException.class,
            readOnly = false,propagation = Propagation.REQUIRED)
    @Override
    public void addTwo2(UserMyBatis userMyBatis1, UserMyBatis userMyBatis2) {
        addOne(userMyBatis1);
        addOne(userMyBatis1);
    }

    @CachePut(key="#p0.id")
    public int insert(UserMyBatis userMyBatis){
        return userMyBatisMapper.insert(userMyBatis.getName(), userMyBatis.getAge());
    }

    @Cacheable
    public UserMyBatis queryById(Long id){
        return  userMyBatisMapper.selectById(id);
    }
}
