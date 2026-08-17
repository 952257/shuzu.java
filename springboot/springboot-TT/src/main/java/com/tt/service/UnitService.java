package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.CommunityGuard;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.FloorMapper;
import com.tt.mapper.UnitMapper;
import com.tt.po.Floor;
import com.tt.po.Unit;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService extends PhysicalServiceImpl<UnitMapper, Unit> {

    @Resource
    private FloorMapper floorMapper;

    public PageResult<Unit> queryUnits(String communityId, String floorId, String unitNum, Integer page, Integer row) {
        LambdaQueryWrapper<Unit> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(floorId)) {
            CommunityGuard.requireFloor(floorId);
            Floor floor = floorMapper.selectById(floorId);
            if (StringUtils.hasText(communityId)) {
                QueryHelper.require(communityId.equals(floor.getCommunityId()), "楼栋不属于该小区");
            }
            wrapper.eq(Unit::getFloorId, floorId);
        } else {
            CommunityGuard.requireCommunity(communityId);
            List<String> floorIds = floorMapper.selectList(new LambdaQueryWrapper<Floor>()
                            .eq(Floor::getCommunityId, communityId))
                    .stream()
                    .map(Floor::getFloorId)
                    .collect(Collectors.toList());
            if (floorIds.isEmpty()) {
                wrapper.eq(Unit::getFloorId, "-1");
            } else {
                wrapper.in(Unit::getFloorId, floorIds);
            }
        }
        wrapper.eq(StringUtils.hasText(unitNum), Unit::getUnitNum, unitNum)
                .orderByAsc(Unit::getUnitNum);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveUnit(Unit unit) {
        CommunityGuard.requireFloor(unit.getFloorId());
        QueryHelper.requireHasText(unit.getUnitNum(), "单元编号不能为空");
        unit.setUnitId(IdGenerator.nextId());
        save(unit);
        return unit.getUnitId();
    }

    public void updateUnit(Unit unit) {
        QueryHelper.requireHasText(unit.getUnitId(), "单元ID不能为空");
        Unit db = getById(unit.getUnitId());
        QueryHelper.require(db != null, "单元不存在");
        CommunityGuard.requireFloor(db.getFloorId());
        unit.setFloorId(db.getFloorId());
        updateById(unit);
    }

    public void deleteUnit(String unitId) {
        QueryHelper.requireHasText(unitId, "单元ID不能为空");
        Unit db = getById(unitId);
        QueryHelper.require(db != null, "单元不存在");
        CommunityGuard.requireFloor(db.getFloorId());
        removeById(unitId);
    }
}
