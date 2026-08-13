package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.CommunityMapper;
import com.tt.po.Community;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommunityService extends ServiceImpl<CommunityMapper, Community> {

    public PageResult<Community> listCommunitys(String communityId, String name, String cityCode, Integer page, Integer row) {
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Community::getCommunityId, communityId)
                .like(StringUtils.hasText(name), Community::getName, name)
                .eq(StringUtils.hasText(cityCode), Community::getCityCode, cityCode)
                .orderByDesc(Community::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveCommunity(Community community) {
        QueryHelper.requireHasText(community.getName(), "小区名称不能为空");
        QueryHelper.requireHasText(community.getAddress(), "小区地址不能为空");
        QueryHelper.requireHasText(community.getCityCode(), "地区编码不能为空");
        community.setCommunityId(IdGenerator.nextId());
        if (!StringUtils.hasText(community.getState())) {
            community.setState("1100");
        }
        save(community);
        return community.getCommunityId();
    }

    public void updateCommunity(Community community) {
        QueryHelper.requireHasText(community.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(community.getName(), "小区名称不能为空");
        QueryHelper.requireHasText(community.getAddress(), "小区地址不能为空");
        QueryHelper.requireHasText(community.getNearbyLandmarks(), "附近地标不能为空");
        updateById(community);
    }

    public void deleteCommunity(String communityId) {
        QueryHelper.requireHasText(communityId, "小区ID不能为空");
        QueryHelper.require(getById(communityId) != null, "小区不存在");
        removeById(communityId);
    }
}
