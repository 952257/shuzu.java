package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.CommunityGuard;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.CommunityMapper;
import com.tt.po.Community;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommunityService extends PhysicalServiceImpl<CommunityMapper, Community> {

    public PageResult<Community> listCommunitys(String communityId, String name, String cityCode, Integer page, Integer row) {
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Community::getCommunityId, communityId)
                .eq(StringUtils.hasText(UserContext.getStoreId()), Community::getStoreId, UserContext.getStoreId())
                .like(StringUtils.hasText(name), Community::getName, name)
                .eq(StringUtils.hasText(cityCode), Community::getCityCode, cityCode)
                .orderByDesc(Community::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveCommunity(Community community) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(community.getName(), "小区名称不能为空");
        QueryHelper.requireHasText(community.getAddress(), "小区地址不能为空");
        QueryHelper.requireHasText(community.getCityCode(), "地区编码不能为空");
        community.setCommunityId(IdGenerator.nextId());
        if (!StringUtils.hasText(community.getState())) {
            community.setState("1100");
        }
        QueryHelper.requireHasText(UserContext.getStoreId(), "请重新登录后再操作");
        community.setStoreId(UserContext.getStoreId());
        save(community);
        return community.getCommunityId();
    }

    public void updateCommunity(Community community) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(community.getCommunityId(), "小区ID不能为空");
        Community db = getById(community.getCommunityId());
        CommunityGuard.mustBelong(db, Community::getCommunityId, "小区不存在");
        QueryHelper.requireHasText(community.getName(), "小区名称不能为空");
        QueryHelper.requireHasText(community.getAddress(), "小区地址不能为空");
        QueryHelper.requireHasText(community.getNearbyLandmarks(), "附近地标不能为空");
        community.setStoreId(db.getStoreId());
        updateById(community);
    }

    public void deleteCommunity(String communityId) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(communityId, "小区ID不能为空");
        Community db = getById(communityId);
        CommunityGuard.mustBelong(db, Community::getCommunityId, "小区不存在");
        removeById(communityId);
    }
}
