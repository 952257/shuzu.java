package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Repair;
import com.tt.service.RepairService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class RepairController {

    @Resource
    private RepairService repairService;

    @GetMapping("/repair.listRepairs")
    public PageResult<Repair> list(@RequestParam(required = false) String communityId,
                                   @RequestParam(required = false) String repairName,
                                   @RequestParam(required = false) String state,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer row) {
        return repairService.listRepairs(communityId, repairName, state, page, row);
    }

    @PostMapping("/repair.saveRepair")
    public Result<String> save(@RequestBody Repair repair) {
        return Result.ok(repairService.saveRepair(repair));
    }

    @PostMapping("/repair.updateRepair")
    public Result<Void> update(@RequestBody Repair repair) {
        repairService.updateRepair(repair);
        return Result.ok();
    }

    @PostMapping("/repair.deleteRepair")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        repairService.deleteRepair(body.get("repairId"));
        return Result.ok();
    }

    @PostMapping("/repair.dispatchRepair")
    public Result<Void> dispatch(@RequestBody Map<String, String> body) {
        repairService.dispatchRepair(body.get("repairId"), body.get("staffId"), body.get("staffName"));
        return Result.ok();
    }

    @PostMapping("/repair.finishRepair")
    public Result<Void> finish(@RequestBody Map<String, String> body) {
        repairService.finishRepair(body.get("repairId"));
        return Result.ok();
    }
}
