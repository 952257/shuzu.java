package com.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.mybatisplus.entity.UserBatch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBatchMapper extends BaseMapper<UserBatch> {
}