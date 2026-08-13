package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerAppUserMapper;
import com.tt.po.OwnerAppUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OwnerAppUserService extends ServiceImpl<OwnerAppUserMapper, OwnerAppUser> {

    public PageResult<OwnerAppUser> listAppUserBindingOwners(String communityId, String state, Integer page, Integer row) {
        LambdaQueryWrapper<OwnerAppUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), OwnerAppUser::getCommunityId, communityId)
                .eq(StringUtils.hasText(state), OwnerAppUser::getState, state)
                .orderByDesc(OwnerAppUser::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveAuth(OwnerAppUser appUser) {
        QueryHelper.requireHasText(appUser.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(appUser.getAppUserName(), "认证人不能为空");
        appUser.setAppUserId(IdGenerator.nextId());
        if (!StringUtils.hasText(appUser.getState())) {
            appUser.setState("10000");
        }
        save(appUser);
        return appUser.getAppUserId();
    }

    public void auditAuthOwner(String appUserId, String state, String remark) {
        QueryHelper.requireHasText(appUserId, "认证ID不能为空");
        OwnerAppUser appUser = getById(appUserId);
        QueryHelper.require(appUser != null, "认证记录不存在");
        appUser.setState(state);
        appUser.setRemark(remark);
        updateById(appUser);
    }
}
