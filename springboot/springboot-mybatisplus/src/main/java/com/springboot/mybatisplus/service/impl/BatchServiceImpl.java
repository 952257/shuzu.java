package com.springboot.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.mybatisplus.entity.UserBatch;
import com.springboot.mybatisplus.mapper.UserBatchMapper;
import com.springboot.mybatisplus.service.UserBatchService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BatchServiceImpl extends ServiceImpl<UserBatchMapper, UserBatch>  implements UserBatchService {

    @Resource
    private SqlSessionFactory sqlSessionFactory;


    public void insertUserBatch(){
        List<UserBatch> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(new UserBatch(null, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        MybatisBatch<UserBatch> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, list);
        MybatisBatch.Method<UserBatch> method = new MybatisBatch.Method<>(UserBatchMapper.class);
        mybatisBatch.execute(method.insert());
    }
}