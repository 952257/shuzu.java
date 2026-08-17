package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.OwnerMapper;
import com.tt.mapper.OwnerRoomRelMapper;
import com.tt.mapper.RoomMapper;
import com.tt.po.Owner;
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
public class RoomService extends PhysicalServiceImpl<RoomMapper, Room> {

    @Resource
    private OwnerRoomRelMapper ownerRoomRelMapper;
    @Resource
    private OwnerMapper ownerMapper;

    public PageResult<Room> queryRooms(String communityId, String unitId, String roomNum, String state, String roomSubType, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getCommunityId, communityId)
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
        CommunityGuard.requireCommunity(room.getCommunityId());
        CommunityGuard.requireUnitInCommunity(room.getUnitId(), room.getCommunityId());
        room.setRoomId(IdGenerator.nextId());
        if (!StringUtils.hasText(room.getState())) {
            room.setState("2001");
        }
        save(room);
        return room.getRoomId();
    }

    public void updateRoom(Room room) {
        QueryHelper.requireHasText(room.getRoomId(), "房屋ID不能为空");
        Room db = getById(room.getRoomId());
        CommunityGuard.mustBelong(db, Room::getCommunityId, "房屋不存在");
        room.setCommunityId(db.getCommunityId());
        if (StringUtils.hasText(room.getUnitId())) {
            CommunityGuard.requireUnitInCommunity(room.getUnitId(), db.getCommunityId());
        } else {
            room.setUnitId(db.getUnitId());
        }
        updateById(room);
    }

    public void deleteRoom(String roomId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        Room db = getById(roomId);
        CommunityGuard.mustBelong(db, Room::getCommunityId, "房屋不存在");
        removeById(roomId);
    }

    @Transactional
    public void sellRoom(String roomId, String ownerId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        QueryHelper.requireHasText(ownerId, "业主ID不能为空");
        Room room = getById(roomId);
        CommunityGuard.mustBelong(room, Room::getCommunityId, "房屋不存在");
        Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getOwnerId, ownerId)
                .eq(Owner::getOwnerTypeCd, "1001")
                .last("limit 1"));
        if (owner == null) {
            owner = ownerMapper.selectById(ownerId);
        }
        CommunityGuard.mustBelong(owner, Owner::getCommunityId, "业主不存在");
        QueryHelper.require(room.getCommunityId().equals(owner.getCommunityId()), "业主不属于该房屋所在小区");
        Long active = ownerRoomRelMapper.selectCount(new LambdaQueryWrapper<OwnerRoomRel>()
                .eq(OwnerRoomRel::getRoomId, roomId)
                .eq(OwnerRoomRel::getState, "2002"));
        QueryHelper.require(active == null || active == 0, "该房屋已出售，请先办理退房");
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
        CommunityGuard.mustBelong(room, Room::getCommunityId, "房屋不存在");
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
        QueryHelper.requireHasText(ownerId, "业主ID不能为空");
        Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getOwnerId, ownerId)
                .eq(Owner::getOwnerTypeCd, "1001")
                .last("limit 1"));
        if (owner == null) {
            owner = ownerMapper.selectById(ownerId);
        }
        CommunityGuard.mustBelong(owner, Owner::getCommunityId, "业主不存在");
        List<OwnerRoomRel> rels = ownerRoomRelMapper.selectList(
                new LambdaQueryWrapper<OwnerRoomRel>().eq(OwnerRoomRel::getOwnerId, ownerId).eq(OwnerRoomRel::getState, "2002"));
        List<String> roomIds = rels.stream().map(OwnerRoomRel::getRoomId).collect(Collectors.toList());
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getCommunityId, owner.getCommunityId());
        if (roomIds.isEmpty()) {
            wrapper.eq(Room::getRoomId, "-1");
        } else {
            wrapper.in(Room::getRoomId, roomIds);
        }
        return QueryHelper.toPage(this, wrapper, page, row);
    }
}
