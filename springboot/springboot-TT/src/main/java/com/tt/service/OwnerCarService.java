package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerCarMapper;
import com.tt.po.OwnerCar;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OwnerCarService extends ServiceImpl<OwnerCarMapper, OwnerCar> {

    public PageResult<OwnerCar> listCars(String communityId, String ownerId, String carNum, Integer page, Integer row) {
        LambdaQueryWrapper<OwnerCar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), OwnerCar::getCommunityId, communityId)
                .eq(StringUtils.hasText(ownerId), OwnerCar::getOwnerId, ownerId)
                .like(StringUtils.hasText(carNum), OwnerCar::getCarNum, carNum)
                .orderByDesc(OwnerCar::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveOwnerCar(OwnerCar car) {
        QueryHelper.requireHasText(car.getOwnerId(), "业主ID不能为空");
        QueryHelper.requireHasText(car.getCarNum(), "车牌号不能为空");
        QueryHelper.requireHasText(car.getCommunityId(), "小区ID不能为空");
        car.setCarId(IdGenerator.nextId());
        save(car);
        return car.getCarId();
    }

    public void updateOwnerCar(OwnerCar car) {
        QueryHelper.requireHasText(car.getCarId(), "车辆ID不能为空");
        updateById(car);
    }

    public void deleteOwnerCar(String carId) {
        QueryHelper.requireHasText(carId, "车辆ID不能为空");
        removeById(carId);
    }
}
