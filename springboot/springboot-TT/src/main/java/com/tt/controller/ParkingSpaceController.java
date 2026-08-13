package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.ParkingSpace;
import com.tt.service.ParkingSpaceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class ParkingSpaceController {

    @Resource
    private ParkingSpaceService parkingSpaceService;

    @GetMapping("/parkingSpace.queryParkingSpaces")
    public PageResult<ParkingSpace> list(@RequestParam(required = false) String communityId,
                                         @RequestParam(required = false) String num,
                                         @RequestParam(required = false) String state,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer row) {
        return parkingSpaceService.queryParkingSpaces(communityId, num, state, page, row);
    }

    @PostMapping("/parkingSpace.saveParkingSpace")
    public Result<String> save(@RequestBody ParkingSpace space) {
        return Result.ok(parkingSpaceService.saveParkingSpace(space));
    }

    @PostMapping("/parkingSpace.editParkingSpace")
    public Result<Void> edit(@RequestBody ParkingSpace space) {
        parkingSpaceService.editParkingSpace(space);
        return Result.ok();
    }

    @PostMapping("/parkingSpace.deleteParkingSpace")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        parkingSpaceService.deleteParkingSpace(body.get("psId"));
        return Result.ok();
    }

    @PostMapping("/parkingSpace.sellParkingSpace")
    public Result<Void> sell(@RequestBody Map<String, String> body) {
        parkingSpaceService.sellParkingSpace(body.get("psId"));
        return Result.ok();
    }

    @PostMapping("/parkingSpace.exitParkingSpace")
    public Result<Void> exit(@RequestBody Map<String, String> body) {
        parkingSpaceService.exitParkingSpace(body.get("psId"));
        return Result.ok();
    }
}
