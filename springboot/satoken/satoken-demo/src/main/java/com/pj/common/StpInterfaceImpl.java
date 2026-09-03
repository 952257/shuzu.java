package com.pj.common;

import cn.dev33.satoken.stp.StpInterface;
import com.pj.mapper.ResourceMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 从数据库加载当前账号拥有的接口权限（resource.path）
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    private final ResourceMapper resourceMapper;

    public StpInterfaceImpl(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Integer userId = Integer.valueOf(loginId.toString());
        List<String> permissions = resourceMapper.selectPermissionPathsByUserId(userId);
        return permissions == null ? Collections.emptyList() : permissions;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Collections.emptyList();
    }
}
