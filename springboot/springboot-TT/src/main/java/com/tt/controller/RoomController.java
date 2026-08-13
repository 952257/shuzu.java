package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Room;
import com.tt.service.RoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class RoomController {

    @Resource
    private RoomService roomService;

    @GetMapping("/room.queryRooms")
    public PageResult<Room> list(@RequestParam(required = false) String communityId,
                                 @RequestParam(required = false) String unitId,
                                 @RequestParam(required = false) String roomNum,
                                 @RequestParam(required = false) String state,
                                 @RequestParam(required = false) String roomSubType,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer row) {
        return roomService.queryRooms(communityId, unitId, roomNum, state, roomSubType, page, row);
    }

    @GetMapping("/room.queryRoomsWithOutSell")
    public PageResult<Room> unsold(@RequestParam(required = false) String communityId,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer row) {
        return roomService.queryRooms(communityId, null, null, "2001", null, page, row);
    }

    @GetMapping("/room.queryRoomsWithSell")
    public PageResult<Room> sold(@RequestParam(required = false) String communityId,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer row) {
        return roomService.queryRooms(communityId, null, null, "2002", null, page, row);
    }

    @GetMapping("/room.queryRoomsByOwner")
    public PageResult<Room> byOwner(@RequestParam String ownerId,
                                    @RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer row) {
        return roomService.queryRoomsByOwner(ownerId, page, row);
    }

    @PostMapping("/room.saveRoom")
    public Result<String> save(@RequestBody Room room) {
        return Result.ok(roomService.saveRoom(room));
    }

    @PostMapping("/room.updateRoom")
    public Result<Void> update(@RequestBody Room room) {
        roomService.updateRoom(room);
        return Result.ok();
    }

    @PostMapping("/room.deleteRoom")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        roomService.deleteRoom(body.get("roomId"));
        return Result.ok();
    }

    @PostMapping("/room.sellRoom")
    public Result<Void> sell(@RequestBody Map<String, String> body) {
        roomService.sellRoom(body.get("roomId"), body.get("ownerId"));
        return Result.ok();
    }

    @PostMapping("/room.exitRoom")
    public Result<Void> exit(@RequestBody Map<String, String> body) {
        roomService.exitRoom(body.get("roomId"), body.get("ownerId"));
        return Result.ok();
    }
}
