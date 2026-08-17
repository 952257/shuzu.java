package com.tt.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.io.Serializable;

public abstract class PhysicalServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Resource
    private PhysicalDelete physicalDelete;

    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        return physicalDelete.byId(getEntityClass(), String.valueOf(id)) > 0;
    }
}
