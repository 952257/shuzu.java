package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.RepairMapper;
import com.tt.po.Repair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RepairService extends ServiceImpl<RepairMapper, Repair> {

    public PageResult<Repair> listRepairs(String communityId, String repairName, String state, Integer page, Integer row) {
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Repair::getCommunityId, communityId)
                .like(StringUtils.hasText(repairName), Repair::getRepairName, repairName)
                .eq(StringUtils.hasText(state), Repair::getState, state)
                .orderByDesc(Repair::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveRepair(Repair repair) {
        QueryHelper.requireHasText(repair.getCommunityId(), "小区ID不能为空");
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
        updateById(repair);
    }

    public void deleteRepair(String repairId) {
        QueryHelper.requireHasText(repairId, "报修ID不能为空");
        removeById(repairId);
    }

    public void dispatchRepair(String repairId, String staffId, String staffName) {
        Repair repair = getById(repairId);
        QueryHelper.require(repair != null, "报修单不存在");
        repair.setStaffId(staffId);
        repair.setStaffName(staffName);
        repair.setState("1100");
        updateById(repair);
    }

    public void finishRepair(String repairId) {
        Repair repair = getById(repairId);
        QueryHelper.require(repair != null, "报修单不存在");
        repair.setState("1200");
        if (!StringUtils.hasText(repair.getStaffId())) {
            repair.setStaffId(UserContext.getUserId());
            repair.setStaffName(UserContext.getUserName());
        }
        updateById(repair);
    }
}
