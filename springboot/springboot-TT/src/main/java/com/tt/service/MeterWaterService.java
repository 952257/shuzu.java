package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.MeterWaterMapper;
import com.tt.po.MeterWater;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MeterWaterService extends ServiceImpl<MeterWaterMapper, MeterWater> {

    public PageResult<MeterWater> listMeterWaters(String communityId, String objId, String meterType, Integer page, Integer row) {
        LambdaQueryWrapper<MeterWater> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), MeterWater::getCommunityId, communityId)
                .eq(StringUtils.hasText(objId), MeterWater::getObjId, objId)
                .eq(StringUtils.hasText(meterType), MeterWater::getMeterType, meterType)
                .orderByDesc(MeterWater::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveMeterWater(MeterWater water) {
        QueryHelper.requireHasText(water.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(water.getObjId(), "抄表对象不能为空");
        QueryHelper.requireHasText(water.getMeterType(), "表类型不能为空");
        water.setWaterId(IdGenerator.nextId());
        save(water);
        return water.getWaterId();
    }

    public void updateMeterWater(MeterWater water) {
        QueryHelper.requireHasText(water.getWaterId(), "抄表ID不能为空");
        updateById(water);
    }

    public void deleteMeterWater(String waterId) {
        QueryHelper.requireHasText(waterId, "抄表ID不能为空");
        removeById(waterId);
    }
}
