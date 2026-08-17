package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerCarMapper;
import com.tt.mapper.OwnerMapper;
import com.tt.po.Owner;
import com.tt.po.OwnerCar;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class OwnerCarService extends PhysicalServiceImpl<OwnerCarMapper, OwnerCar> {

    @Resource
    private OwnerMapper ownerMapper;

    public PageResult<OwnerCar> listCars(String communityId, String ownerId, String carNum, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<OwnerCar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerCar::getCommunityId, communityId)
                .eq(StringUtils.hasText(ownerId), OwnerCar::getOwnerId, ownerId)
                .like(StringUtils.hasText(carNum), OwnerCar::getCarNum, carNum)
                .orderByDesc(OwnerCar::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveOwnerCar(OwnerCar car) {
        QueryHelper.requireHasText(car.getOwnerId(), "业主ID不能为空");
        QueryHelper.requireHasText(car.getCarNum(), "车牌号不能为空");
        CommunityGuard.requireCommunity(car.getCommunityId());
        Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getOwnerId, car.getOwnerId())
                .eq(Owner::getOwnerTypeCd, "1001")
                .last("limit 1"));
        if (owner == null) {
            owner = ownerMapper.selectById(car.getOwnerId());
        }
        CommunityGuard.mustBelong(owner, Owner::getCommunityId, "业主不存在");
        QueryHelper.require(car.getCommunityId().equals(owner.getCommunityId()), "业主不属于该小区");
        car.setCarId(IdGenerator.nextId());
        save(car);
        return car.getCarId();
    }

    public void updateOwnerCar(OwnerCar car) {
        QueryHelper.requireHasText(car.getCarId(), "车辆ID不能为空");
        OwnerCar db = getById(car.getCarId());
        CommunityGuard.mustBelong(db, OwnerCar::getCommunityId, "车辆不存在");
        car.setCommunityId(db.getCommunityId());
        updateById(car);
    }

    public void deleteOwnerCar(String carId) {
        QueryHelper.requireHasText(carId, "车辆ID不能为空");
        OwnerCar db = getById(carId);
        CommunityGuard.mustBelong(db, OwnerCar::getCommunityId, "车辆不存在");
        removeById(carId);
    }
}
