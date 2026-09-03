package com.pj.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pj.entity.SysUser;
import com.pj.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {

    private final SysUserMapper sysUserMapper;

    public SysUserService(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    public SysUser login(String username, String password) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getPassword, password));
    }
}
