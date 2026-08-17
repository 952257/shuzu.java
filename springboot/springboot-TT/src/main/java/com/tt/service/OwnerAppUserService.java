package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerAppUserMapper;
import com.tt.mapper.OwnerMapper;
import com.tt.po.Owner;
import com.tt.po.OwnerAppUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class OwnerAppUserService extends PhysicalServiceImpl<OwnerAppUserMapper, OwnerAppUser> {

    @Resource
    private OwnerMapper ownerMapper;

    public PageResult<OwnerAppUser> listAppUserBindingOwners(String communityId, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<OwnerAppUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerAppUser::getCommunityId, communityId)
                .eq(StringUtils.hasText(state), OwnerAppUser::getState, state)
                .orderByDesc(OwnerAppUser::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveAuth(OwnerAppUser appUser) {
        CommunityGuard.requireCommunity(appUser.getCommunityId());
        QueryHelper.requireHasText(appUser.getAppUserName(), "认证人不能为空");
        if (StringUtils.hasText(appUser.getMemberId())) {
            Owner owner = ownerMapper.selectById(appUser.getMemberId());
            if (owner == null) {
                owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                        .eq(Owner::getOwnerId, appUser.getMemberId())
                        .eq(Owner::getOwnerTypeCd, "1001")
                        .last("limit 1"));
            }
            CommunityGuard.mustBelong(owner, Owner::getCommunityId, "业主不存在");
            QueryHelper.require(appUser.getCommunityId().equals(owner.getCommunityId()), "认证业主不属于该小区");
        }
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
        CommunityGuard.mustBelong(appUser, OwnerAppUser::getCommunityId, "认证记录不存在");
        appUser.setState(state);
        appUser.setRemark(remark);
        updateById(appUser);
    }
}
