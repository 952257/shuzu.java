package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerRoomRelMapper;
import com.tt.mapper.RoomMapper;
import com.tt.po.OwnerRoomRel;
import com.tt.po.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService extends ServiceImpl<RoomMapper, Room> {

    @Resource
    private OwnerRoomRelMapper ownerRoomRelMapper;

    public PageResult<Room> queryRooms(String communityId, String unitId, String roomNum, String state, String roomSubType, Integer page, Integer row) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), Room::getCommunityId, communityId)
                .eq(StringUtils.hasText(unitId), Room::getUnitId, unitId)
                .like(StringUtils.hasText(roomNum), Room::getRoomNum, roomNum)
                .eq(StringUtils.hasText(state), Room::getState, state)
                .eq(StringUtils.hasText(roomSubType), Room::getRoomSubType, roomSubType)
                .orderByAsc(Room::getRoomNum);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveRoom(Room room) {
        QueryHelper.requireHasText(room.getUnitId(), "单元ID不能为空");
        QueryHelper.requireHasText(room.getRoomNum(), "房屋编号不能为空");
        QueryHelper.requireHasText(room.getCommunityId(), "小区ID不能为空");
        room.setRoomId(IdGenerator.nextId());
        if (!StringUtils.hasText(room.getState())) {
            room.setState("2001");
        }
        save(room);
        return room.getRoomId();
    }

    public void updateRoom(Room room) {
        QueryHelper.requireHasText(room.getRoomId(), "房屋ID不能为空");
        updateById(room);
    }

    public void deleteRoom(String roomId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        removeById(roomId);
    }

    @Transactional
    public void sellRoom(String roomId, String ownerId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        QueryHelper.requireHasText(ownerId, "业主ID不能为空");
        Room room = getById(roomId);
        QueryHelper.require(room != null, "房屋不存在");
        room.setState("2002");
        updateById(room);
        OwnerRoomRel rel = new OwnerRoomRel();
        rel.setRelId(IdGenerator.nextId());
        rel.setOwnerId(ownerId);
        rel.setRoomId(roomId);
        rel.setState("2002");
        rel.setStartTime(new Date());
        ownerRoomRelMapper.insert(rel);
    }

    @Transactional
    public void exitRoom(String roomId, String ownerId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        Room room = getById(roomId);
        QueryHelper.require(room != null, "房屋不存在");
        room.setState("2001");
        updateById(room);
        LambdaQueryWrapper<OwnerRoomRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoomRel::getRoomId, roomId)
                .eq(StringUtils.hasText(ownerId), OwnerRoomRel::getOwnerId, ownerId)
                .eq(OwnerRoomRel::getState, "2002");
        List<OwnerRoomRel> rels = ownerRoomRelMapper.selectList(wrapper);
        for (OwnerRoomRel rel : rels) {
            rel.setState("2003");
            rel.setEndTime(new Date());
            ownerRoomRelMapper.updateById(rel);
        }
    }

    public PageResult<Room> queryRoomsByOwner(String ownerId, Integer page, Integer row) {
        List<OwnerRoomRel> rels = ownerRoomRelMapper.selectList(
                new LambdaQueryWrapper<OwnerRoomRel>().eq(OwnerRoomRel::getOwnerId, ownerId).eq(OwnerRoomRel::getState, "2002"));
        List<String> roomIds = rels.stream().map(OwnerRoomRel::getRoomId).collect(Collectors.toList());
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        if (roomIds.isEmpty()) {
            wrapper.eq(Room::getRoomId, "-1");
        } else {
            wrapper.in(Room::getRoomId, roomIds);
        }
        return QueryHelper.toPage(this, wrapper, page, row);
    }
}
