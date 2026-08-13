package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.UnitMapper;
import com.tt.po.Unit;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UnitService extends ServiceImpl<UnitMapper, Unit> {

    public PageResult<Unit> queryUnits(String floorId, String unitNum, Integer page, Integer row) {
        LambdaQueryWrapper<Unit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(floorId), Unit::getFloorId, floorId)
                .eq(StringUtils.hasText(unitNum), Unit::getUnitNum, unitNum)
                .orderByAsc(Unit::getUnitNum);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveUnit(Unit unit) {
        QueryHelper.requireHasText(unit.getFloorId(), "楼栋ID不能为空");
        QueryHelper.requireHasText(unit.getUnitNum(), "单元编号不能为空");
        unit.setUnitId(IdGenerator.nextId());
        save(unit);
        return unit.getUnitId();
    }

    public void updateUnit(Unit unit) {
        QueryHelper.requireHasText(unit.getUnitId(), "单元ID不能为空");
        updateById(unit);
    }

    public void deleteUnit(String unitId) {
        QueryHelper.requireHasText(unitId, "单元ID不能为空");
        removeById(unitId);
    }
}
