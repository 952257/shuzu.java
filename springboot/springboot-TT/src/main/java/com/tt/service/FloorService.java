package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.FloorMapper;
import com.tt.po.Floor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FloorService extends PhysicalServiceImpl<FloorMapper, Floor> {

    public PageResult<Floor> queryFloors(String communityId, String floorNum, String name, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Floor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Floor::getCommunityId, communityId)
                .eq(StringUtils.hasText(floorNum), Floor::getFloorNum, floorNum)
                .like(StringUtils.hasText(name), Floor::getName, name)
                .orderByAsc(Floor::getSeq);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveFloor(Floor floor) {
        CommunityGuard.requireCommunity(floor.getCommunityId());
        QueryHelper.requireHasText(floor.getFloorNum(), "楼栋编号不能为空");
        QueryHelper.requireHasText(floor.getName(), "楼栋名称不能为空");
        floor.setFloorId(IdGenerator.nextId());
        save(floor);
        return floor.getFloorId();
    }

    public void editFloor(Floor floor) {
        QueryHelper.requireHasText(floor.getFloorId(), "楼栋ID不能为空");
        Floor db = getById(floor.getFloorId());
        CommunityGuard.mustBelong(db, Floor::getCommunityId, "楼栋不存在");
        floor.setCommunityId(db.getCommunityId());
        updateById(floor);
    }

    public void deleteFloor(String floorId) {
        QueryHelper.requireHasText(floorId, "楼栋ID不能为空");
        Floor db = getById(floorId);
        CommunityGuard.mustBelong(db, Floor::getCommunityId, "楼栋不存在");
        removeById(floorId);
    }
}
