package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerMapper;
import com.tt.po.Owner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OwnerService extends ServiceImpl<OwnerMapper, Owner> {

    public PageResult<Owner> queryOwners(String communityId, String name, String link, String ownerTypeCd, Integer page, Integer row) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Owner::getCommunityId, communityId)
                .like(StringUtils.hasText(name), Owner::getName, name)
                .like(StringUtils.hasText(link), Owner::getLink, link)
                .eq(StringUtils.hasText(ownerTypeCd), Owner::getOwnerTypeCd, ownerTypeCd)
                .orderByDesc(Owner::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveOwner(Owner owner) {
        QueryHelper.requireHasText(owner.getName(), "业主姓名不能为空");
        QueryHelper.requireHasText(owner.getLink(), "手机号不能为空");
        String id = IdGenerator.nextId();
        owner.setMemberId(id);
        if (!StringUtils.hasText(owner.getOwnerId())) {
            owner.setOwnerId(id);
        }
        if (!StringUtils.hasText(owner.getOwnerTypeCd())) {
            owner.setOwnerTypeCd("1001");
        }
        if (!StringUtils.hasText(owner.getState())) {
            owner.setState("2000");
        }
        save(owner);
        return owner.getMemberId();
    }

    public void editOwner(Owner owner) {
        QueryHelper.requireHasText(owner.getMemberId(), "业主ID不能为空");
        updateById(owner);
    }

    public void deleteOwner(String memberId) {
        QueryHelper.requireHasText(memberId, "业主ID不能为空");
        removeById(memberId);
    }

    public PageResult<Owner> queryOwnerMembers(String ownerId, Integer page, Integer row) {
        QueryHelper.requireHasText(ownerId, "业主ID不能为空");
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Owner::getOwnerId, ownerId).eq(Owner::getOwnerTypeCd, "1002");
        return QueryHelper.toPage(this, wrapper, page, row);
    }
}
