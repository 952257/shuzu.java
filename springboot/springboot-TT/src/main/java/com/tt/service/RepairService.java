package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.RepairMapper;
import com.tt.po.Repair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RepairService extends PhysicalServiceImpl<RepairMapper, Repair> {

    public PageResult<Repair> listRepairs(String communityId, String repairName, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Repair::getCommunityId, communityId)
                .like(StringUtils.hasText(repairName), Repair::getRepairName, repairName)
                .eq(StringUtils.hasText(state), Repair::getState, state)
                .orderByDesc(Repair::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveRepair(Repair repair) {
        CommunityGuard.requireCommunity(repair.getCommunityId());
        QueryHelper.requireHasText(repair.getRepairName(), "报修人不能为空");
        repair.setRepairId(IdGenerator.nextId());
        if (!StringUtils.hasText(repair.getState())) {
            repair.setState("1000");
        }
        save(repair);
        return repair.getRepairId();
    }

    public void updateRepair(Repair repair) {
        QueryHelper.requireHasText(repair.getRepairId(), "报修ID不能为空");
        Repair db = getById(repair.getRepairId());
        CommunityGuard.mustBelong(db, Repair::getCommunityId, "报修单不存在");
        repair.setCommunityId(db.getCommunityId());
        updateById(repair);
    }

    public void deleteRepair(String repairId) {
        QueryHelper.requireHasText(repairId, "报修ID不能为空");
        Repair db = getById(repairId);
        CommunityGuard.mustBelong(db, Repair::getCommunityId, "报修单不存在");
        removeById(repairId);
    }

    public void dispatchRepair(String repairId, String staffId, String staffName) {
        Repair repair = getById(repairId);
        CommunityGuard.mustBelong(repair, Repair::getCommunityId, "报修单不存在");
        repair.setStaffId(staffId);
        repair.setStaffName(staffName);
        repair.setState("1100");
        updateById(repair);
    }

    public void finishRepair(String repairId) {
        Repair repair = getById(repairId);
        CommunityGuard.mustBelong(repair, Repair::getCommunityId, "报修单不存在");
        repair.setState("1200");
        if (!StringUtils.hasText(repair.getStaffId())) {
            repair.setStaffId(UserContext.getUserId());
            repair.setStaffName(UserContext.getUserName());
        }
        updateById(repair);
    }
}
