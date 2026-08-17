package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerMapper;
import com.tt.po.Owner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OwnerService extends PhysicalServiceImpl<OwnerMapper, Owner> {

    public PageResult<Owner> queryOwners(String communityId, String name, String link, String ownerTypeCd, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Owner::getCommunityId, communityId)
                .like(StringUtils.hasText(name), Owner::getName, name)
                .like(StringUtils.hasText(link), Owner::getLink, link)
                .eq(StringUtils.hasText(ownerTypeCd), Owner::getOwnerTypeCd, ownerTypeCd)
                .orderByDesc(Owner::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveOwner(Owner owner) {
        QueryHelper.requireHasText(owner.getName(), "业主姓名不能为空");
        QueryHelper.requireHasText(owner.getLink(), "手机号不能为空");
        CommunityGuard.requireCommunity(owner.getCommunityId());
        String id = IdGenerator.nextId();
        owner.setMemberId(id);
        if (!StringUtils.hasText(owner.getOwnerTypeCd()) || "1001".equals(owner.getOwnerTypeCd())) {
            owner.setOwnerTypeCd("1001");
            owner.setOwnerId(id);
        } else {
            QueryHelper.requireHasText(owner.getOwnerId(), "业主ID不能为空");
            Owner parent = getOne(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getOwnerId, owner.getOwnerId())
                    .eq(Owner::getOwnerTypeCd, "1001")
                    .last("limit 1"));
            if (parent == null) {
                parent = getById(owner.getOwnerId());
            }
            CommunityGuard.mustBelong(parent, Owner::getCommunityId, "业主不存在");
            QueryHelper.require(owner.getCommunityId().equals(parent.getCommunityId()), "成员不属于该业主所在小区");
        }
        if (!StringUtils.hasText(owner.getState())) {
            owner.setState("2000");
        }
        save(owner);
        return owner.getMemberId();
    }

    public void editOwner(Owner owner) {
        QueryHelper.requireHasText(owner.getMemberId(), "业主ID不能为空");
        Owner db = getById(owner.getMemberId());
        CommunityGuard.mustBelong(db, Owner::getCommunityId, "业主不存在");
        owner.setCommunityId(db.getCommunityId());
        updateById(owner);
    }

    public void deleteOwner(String memberId) {
        QueryHelper.requireHasText(memberId, "业主ID不能为空");
        Owner db = getById(memberId);
        CommunityGuard.mustBelong(db, Owner::getCommunityId, "业主不存在");
        removeById(memberId);
    }

    public PageResult<Owner> queryOwnerMembers(String ownerId, Integer page, Integer row) {
        QueryHelper.requireHasText(ownerId, "业主ID不能为空");
        Owner owner = getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getOwnerId, ownerId).eq(Owner::getOwnerTypeCd, "1001").last("limit 1"));
        if (owner == null) {
            owner = getById(ownerId);
        }
        CommunityGuard.mustBelong(owner, Owner::getCommunityId, "业主不存在");
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Owner::getOwnerId, ownerId).eq(Owner::getOwnerTypeCd, "1002");
        return QueryHelper.toPage(this, wrapper, page, row);
    }
}
