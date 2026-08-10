package com.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.mybatisplus.entity.OptimisticLock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptimisticLockMapper extends BaseMapper<OptimisticLock> {
}