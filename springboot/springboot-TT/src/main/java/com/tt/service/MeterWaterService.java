package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.MeterWaterMapper;
import com.tt.mapper.RoomMapper;
import com.tt.po.MeterWater;
import com.tt.po.Room;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MeterWaterService extends PhysicalServiceImpl<MeterWaterMapper, MeterWater> {

    @Resource
    private RoomMapper roomMapper;

    public PageResult<MeterWater> listMeterWaters(String communityId, String objId, String meterType, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<MeterWater> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeterWater::getCommunityId, communityId)
                .eq(StringUtils.hasText(objId), MeterWater::getObjId, objId)
                .eq(StringUtils.hasText(meterType), MeterWater::getMeterType, meterType)
                .orderByDesc(MeterWater::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveMeterWater(MeterWater water) {
        CommunityGuard.requireCommunity(water.getCommunityId());
        QueryHelper.requireHasText(water.getObjId(), "抄表对象不能为空");
        QueryHelper.requireHasText(water.getMeterType(), "表类型不能为空");
        Room room = roomMapper.selectById(water.getObjId());
        CommunityGuard.mustBelong(room, Room::getCommunityId, "房屋不存在");
        QueryHelper.require(water.getCommunityId().equals(room.getCommunityId()), "抄表对象不属于该小区");
        water.setWaterId(IdGenerator.nextId());
        save(water);
        return water.getWaterId();
    }

    public void updateMeterWater(MeterWater water) {
        QueryHelper.requireHasText(water.getWaterId(), "抄表ID不能为空");
        MeterWater db = getById(water.getWaterId());
        CommunityGuard.mustBelong(db, MeterWater::getCommunityId, "抄表记录不存在");
        water.setCommunityId(db.getCommunityId());
        updateById(water);
    }

    public void deleteMeterWater(String waterId) {
        QueryHelper.requireHasText(waterId, "抄表ID不能为空");
        MeterWater db = getById(waterId);
        CommunityGuard.mustBelong(db, MeterWater::getCommunityId, "抄表记录不存在");
        removeById(waterId);
    }
}
