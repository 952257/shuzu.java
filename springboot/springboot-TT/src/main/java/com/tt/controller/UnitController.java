package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Unit;
import com.tt.service.UnitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class UnitController {

    @Resource
    private UnitService unitService;

    @GetMapping("/unit.queryUnits")
    public PageResult<Unit> list(@RequestParam(required = false) String floorId,
                                 @RequestParam(required = false) String unitNum,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer row) {
        return unitService.queryUnits(floorId, unitNum, page, row);
    }

    @PostMapping("/unit.saveUnit")
    public Result<String> save(@RequestBody Unit unit) {
        return Result.ok(unitService.saveUnit(unit));
    }

    @PostMapping("/unit.updateUnit")
    public Result<Void> update(@RequestBody Unit unit) {
        unitService.updateUnit(unit);
        return Result.ok();
    }

    @PostMapping("/unit.deleteUnit")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        unitService.deleteUnit(body.get("unitId"));
        return Result.ok();
    }
}
