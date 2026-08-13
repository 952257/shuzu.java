package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.FloorMapper;
import com.tt.po.Floor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FloorService extends ServiceImpl<FloorMapper, Floor> {

    public PageResult<Floor> queryFloors(String communityId, String floorNum, String name, Integer page, Integer row) {
        LambdaQueryWrapper<Floor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Floor::getCommunityId, communityId)
                .eq(StringUtils.hasText(floorNum), Floor::getFloorNum, floorNum)
                .like(StringUtils.hasText(name), Floor::getName, name)
                .orderByAsc(Floor::getSeq);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveFloor(Floor floor) {
        QueryHelper.requireHasText(floor.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(floor.getFloorNum(), "楼栋编号不能为空");
        QueryHelper.requireHasText(floor.getName(), "楼栋名称不能为空");
        floor.setFloorId(IdGenerator.nextId());
        save(floor);
        return floor.getFloorId();
    }

    public void editFloor(Floor floor) {
        QueryHelper.requireHasText(floor.getFloorId(), "楼栋ID不能为空");
        updateById(floor);
    }

    public void deleteFloor(String floorId) {
        QueryHelper.requireHasText(floorId, "楼栋ID不能为空");
        removeById(floorId);
    }
}
