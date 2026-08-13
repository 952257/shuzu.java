package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Floor;
import com.tt.service.FloorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class FloorController {

    @Resource
    private FloorService floorService;

    @GetMapping("/floor.queryFloors")
    public PageResult<Floor> list(@RequestParam(required = false) String communityId,
                                  @RequestParam(required = false) String floorNum,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer row) {
        return floorService.queryFloors(communityId, floorNum, name, page, row);
    }

    @PostMapping("/floor.saveFloor")
    public Result<String> save(@RequestBody Floor floor) {
        return Result.ok(floorService.saveFloor(floor));
    }

    @PostMapping("/floor.editFloor")
    public Result<Void> edit(@RequestBody Floor floor) {
        floorService.editFloor(floor);
        return Result.ok();
    }

    @PostMapping("/floor.deleteFloor")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        floorService.deleteFloor(body.get("floorId"));
        return Result.ok();
    }
}
