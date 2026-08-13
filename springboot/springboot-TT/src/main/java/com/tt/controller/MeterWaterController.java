package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.MeterWater;
import com.tt.service.MeterWaterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class MeterWaterController {

    @Resource
    private MeterWaterService meterWaterService;

    @GetMapping("/meterWater.listMeterWaters")
    public PageResult<MeterWater> list(@RequestParam(required = false) String communityId,
                                       @RequestParam(required = false) String objId,
                                       @RequestParam(required = false) String meterType,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer row) {
        return meterWaterService.listMeterWaters(communityId, objId, meterType, page, row);
    }

    @PostMapping("/meterWater.saveMeterWater")
    public Result<String> save(@RequestBody MeterWater water) {
        return Result.ok(meterWaterService.saveMeterWater(water));
    }

    @PostMapping("/meterWater.updateMeterWater")
    public Result<Void> update(@RequestBody MeterWater water) {
        meterWaterService.updateMeterWater(water);
        return Result.ok();
    }

    @PostMapping("/meterWater.deleteMeterWater")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        meterWaterService.deleteMeterWater(body.get("waterId"));
        return Result.ok();
    }
}
