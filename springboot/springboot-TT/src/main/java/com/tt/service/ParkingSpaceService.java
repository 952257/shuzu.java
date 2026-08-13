package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.ParkingSpaceMapper;
import com.tt.po.ParkingSpace;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ParkingSpaceService extends ServiceImpl<ParkingSpaceMapper, ParkingSpace> {

    public PageResult<ParkingSpace> queryParkingSpaces(String communityId, String num, String state, Integer page, Integer row) {
        LambdaQueryWrapper<ParkingSpace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), ParkingSpace::getCommunityId, communityId)
                .like(StringUtils.hasText(num), ParkingSpace::getNum, num)
                .eq(StringUtils.hasText(state), ParkingSpace::getState, state)
                .orderByAsc(ParkingSpace::getNum);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveParkingSpace(ParkingSpace space) {
        QueryHelper.requireHasText(space.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(space.getNum(), "车位编号不能为空");
        space.setPsId(IdGenerator.nextId());
        if (!StringUtils.hasText(space.getState())) {
            space.setState("F");
        }
        save(space);
        return space.getPsId();
    }

    public void editParkingSpace(ParkingSpace space) {
        QueryHelper.requireHasText(space.getPsId(), "车位ID不能为空");
        updateById(space);
    }

    public void deleteParkingSpace(String psId) {
        QueryHelper.requireHasText(psId, "车位ID不能为空");
        removeById(psId);
    }

    public void sellParkingSpace(String psId) {
        ParkingSpace space = getById(psId);
        QueryHelper.require(space != null, "车位不存在");
        space.setState("S");
        updateById(space);
    }

    public void exitParkingSpace(String psId) {
        ParkingSpace space = getById(psId);
        QueryHelper.require(space != null, "车位不存在");
        space.setState("F");
        updateById(space);
    }
}
