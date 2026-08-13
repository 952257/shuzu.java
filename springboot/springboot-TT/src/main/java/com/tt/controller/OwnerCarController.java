package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.OwnerCar;
import com.tt.service.OwnerCarService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class OwnerCarController {

    @Resource
    private OwnerCarService ownerCarService;

    @GetMapping("/owner.listOwnerCars")
    public PageResult<OwnerCar> list(@RequestParam(required = false) String communityId,
                                     @RequestParam(required = false) String ownerId,
                                     @RequestParam(required = false) String carNum,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer row) {
        return ownerCarService.listCars(communityId, ownerId, carNum, page, row);
    }

    @PostMapping("/owner.saveOwnerCar")
    public Result<String> save(@RequestBody OwnerCar car) {
        return Result.ok(ownerCarService.saveOwnerCar(car));
    }

    @PostMapping("/owner.updateOwnerCar")
    public Result<Void> update(@RequestBody OwnerCar car) {
        ownerCarService.updateOwnerCar(car);
        return Result.ok();
    }

    @PostMapping("/owner.deleteOwnerCar")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        ownerCarService.deleteOwnerCar(body.get("carId"));
        return Result.ok();
    }
}
