package com.tt.common;

import com.tt.mapper.CommunityMapper;
import com.tt.mapper.FloorMapper;
import com.tt.mapper.UnitMapper;
import com.tt.po.Community;
import com.tt.po.Floor;
import com.tt.po.Unit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.function.Function;

@Component
public class CommunityGuard {

    private static CommunityGuard instance;

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private FloorMapper floorMapper;
    @Resource
    private UnitMapper unitMapper;

    @PostConstruct
    public void register() {
        instance = this;
    }

    public static void requireCommunity(String communityId) {
        QueryHelper.requireCommunityId(communityId);
        QueryHelper.require(instance != null, "系统未就绪，请稍后重试");
        instance.checkOwned(communityId);
    }

    public static <T> T mustBelong(T entity, Function<T, String> communityIdGetter, String notFoundMsg) {
        QueryHelper.require(entity != null, notFoundMsg);
        requireCommunity(communityIdGetter.apply(entity));
        return entity;
    }

    public static void requireFloor(String floorId) {
        QueryHelper.requireHasText(floorId, "楼栋ID不能为空");
        QueryHelper.require(instance != null, "系统未就绪，请稍后重试");
        Floor floor = instance.floorMapper.selectById(floorId);
        QueryHelper.require(floor != null, "楼栋不存在");
        requireCommunity(floor.getCommunityId());
    }

    public static void requireUnitInCommunity(String unitId, String communityId) {
        QueryHelper.requireHasText(unitId, "单元ID不能为空");
        requireCommunity(communityId);
        Unit unit = instance.unitMapper.selectById(unitId);
        QueryHelper.require(unit != null, "单元不存在");
        Floor floor = instance.floorMapper.selectById(unit.getFloorId());
        QueryHelper.require(floor != null, "楼栋不存在");
        QueryHelper.require(communityId.equals(floor.getCommunityId()), "单元不属于该小区");
    }

    private void checkOwned(String communityId) {
        Community community = communityMapper.selectById(communityId);
        QueryHelper.require(community != null, "小区不存在");
        String storeId = UserContext.getStoreId();
        QueryHelper.require(StringUtils.hasText(storeId), "请重新登录后再操作");
        if (!storeId.equals(community.getStoreId())) {
            throw new ServiceException(ServiceExceptionEnum.FORBIDDEN);
        }
    }
}
