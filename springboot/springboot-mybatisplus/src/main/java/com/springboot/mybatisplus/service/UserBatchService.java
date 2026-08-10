package com.springboot.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.mybatisplus.entity.UserBatch;

public interface UserBatchService extends IService<UserBatch> {

    void insertUserBatch();
}
