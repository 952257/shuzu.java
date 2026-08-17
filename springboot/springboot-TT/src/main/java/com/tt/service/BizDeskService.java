package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.PasswordUtil;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.*;
import com.tt.po.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BizDeskService {

    @Resource
    private FloorMapper floorMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private OwnerMapper ownerMapper;
    @Resource
    private OwnerRoomRelMapper ownerRoomRelMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserLoginMapper userLoginMapper;
    @Resource
    private CommunitySettingMapper communitySettingMapper;
    @Resource
    private StoreUserMapper storeUserMapper;

    public Map<String, Object> queryRoomByCode(String communityId, String roomCode) {
        CommunityGuard.requireCommunity(communityId);
        QueryHelper.requireHasText(roomCode, "请输入房屋编号");
        Room room = findRoom(communityId, roomCode.trim());
        QueryHelper.require(room != null, "未找到房屋，请按 楼栋-单元-房号 输入，如 1-1-101");
        return buildDesk(room);
    }

    public Map<String, Object> queryDeskByRoomId(String roomId) {
        QueryHelper.requireHasText(roomId, "房屋ID不能为空");
        Room room = roomMapper.selectById(roomId);
        CommunityGuard.mustBelong(room, Room::getCommunityId, "房屋不存在");
        return buildDesk(room);
    }

    public List<Map<String, Object>> listRoomOptions(String communityId) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getCommunityId, communityId)
                .orderByAsc(Room::getRoomNum);
        List<Room> rooms = roomMapper.selectList(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Room room : rooms) {
            Map<String, Object> item = new HashMap<>();
            item.put("roomId", room.getRoomId());
            item.put("roomNum", room.getRoomNum());
            item.put("roomName", roomNameOf(room));
            item.put("state", room.getState());
            item.put("roomSubType", room.getRoomSubType());
            list.add(item);
        }
        return list;
    }

    public void changePassword(String oldPass, String newPass) {
        QueryHelper.requireHasText(oldPass, "请输入原密码");
        QueryHelper.requireHasText(newPass, "请输入新密码");
        User user = userMapper.selectById(UserContext.getUserId());
        QueryHelper.require(user != null, "用户不存在");
        QueryHelper.require(PasswordUtil.matches(oldPass, user.getPassword()), "原密码不正确");
        user.setPassword(PasswordUtil.encode(newPass));
        userMapper.updateById(user);
    }

    public PageResult<UserLogin> listLoginLogs(Integer page, Integer row) {
        LambdaQueryWrapper<UserLogin> wrapper = new LambdaQueryWrapper<>();
        if (!UserContext.isAdmin()) {
            wrapper.eq(UserLogin::getUserId, UserContext.getUserId());
        } else {
            QueryHelper.requireHasText(UserContext.getStoreId(), "请重新登录后再操作");
            List<String> userIds = storeUserMapper.selectList(new LambdaQueryWrapper<StoreUser>()
                            .eq(StoreUser::getStoreId, UserContext.getStoreId()))
                    .stream()
                    .map(StoreUser::getUserId)
                    .collect(java.util.stream.Collectors.toList());
            if (userIds.isEmpty()) {
                wrapper.eq(UserLogin::getUserId, "-1");
            } else {
                wrapper.in(UserLogin::getUserId, userIds);
            }
        }
        wrapper.orderByDesc(UserLogin::getLoginTime);
        int p = QueryHelper.page(page);
        int r = QueryHelper.row(row);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserLogin> mp =
                userLoginMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(p, r), wrapper);
        return PageResult.of(mp.getRecords(), mp.getTotal(), p, r);
    }

    public List<CommunitySetting> listSettings(String communityId, String settingGroup) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<CommunitySetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunitySetting::getCommunityId, communityId)
                .eq(StringUtils.hasText(settingGroup), CommunitySetting::getSettingGroup, settingGroup)
                .orderByAsc(CommunitySetting::getSettingKey);
        return communitySettingMapper.selectList(wrapper);
    }

    public void saveSettings(String communityId, List<CommunitySetting> settings) {
        CommunityGuard.requireCommunity(communityId);
        QueryHelper.require(settings != null && !settings.isEmpty(), "配置不能为空");
        for (CommunitySetting item : settings) {
            QueryHelper.requireHasText(item.getSettingKey(), "配置项不能为空");
            CommunitySetting db = communitySettingMapper.selectOne(new LambdaQueryWrapper<CommunitySetting>()
                    .eq(CommunitySetting::getCommunityId, communityId)
                    .eq(CommunitySetting::getSettingKey, item.getSettingKey()));
            if (db == null) {
                item.setSettingId(IdGenerator.nextId());
                item.setCommunityId(communityId);
                communitySettingMapper.insert(item);
            } else {
                db.setSettingValue(item.getSettingValue());
                if (StringUtils.hasText(item.getSettingName())) {
                    db.setSettingName(item.getSettingName());
                }
                communitySettingMapper.updateById(db);
            }
        }
    }

    private Room findRoom(String communityId, String roomCode) {
        String[] parts = roomCode.split("[-/]");
        if (parts.length >= 3) {
            String floorNum = parts[0].trim();
            String unitNum = parts[1].trim();
            String roomNum = parts[2].trim();
            Floor floor = floorMapper.selectOne(new LambdaQueryWrapper<Floor>()
                    .eq(Floor::getCommunityId, communityId)
                    .eq(Floor::getFloorNum, floorNum)
                    .last("limit 1"));
            if (floor == null) {
                return null;
            }
            Unit unit = unitMapper.selectOne(new LambdaQueryWrapper<Unit>()
                    .eq(Unit::getFloorId, floor.getFloorId())
                    .eq(Unit::getUnitNum, unitNum)
                    .last("limit 1"));
            if (unit == null) {
                return null;
            }
            return roomMapper.selectOne(new LambdaQueryWrapper<Room>()
                    .eq(Room::getUnitId, unit.getUnitId())
                    .eq(Room::getRoomNum, roomNum)
                    .last("limit 1"));
        }
        return roomMapper.selectOne(new LambdaQueryWrapper<Room>()
                .eq(Room::getCommunityId, communityId)
                .eq(Room::getRoomNum, roomCode)
                .last("limit 1"));
    }

    private Map<String, Object> buildDesk(Room room) {
        Unit unit = unitMapper.selectById(room.getUnitId());
        Floor floor = unit == null ? null : floorMapper.selectById(unit.getFloorId());
        List<OwnerRoomRel> rels = ownerRoomRelMapper.selectList(new LambdaQueryWrapper<OwnerRoomRel>()
                .eq(OwnerRoomRel::getRoomId, room.getRoomId())
                .orderByDesc(OwnerRoomRel::getCreateTime));
        Owner owner = null;
        Date inDate = null;
        for (OwnerRoomRel rel : rels) {
            if ("2002".equals(rel.getState())) {
                Owner candidate = ownerMapper.selectById(rel.getOwnerId());
                if (candidate != null && !"1002".equals(candidate.getOwnerTypeCd())) {
                    owner = candidate;
                    inDate = rel.getStartTime();
                    break;
                }
                if (owner == null) {
                    owner = candidate;
                    inDate = rel.getStartTime();
                }
            }
        }
        Account account = null;
        if (owner != null) {
            account = accountMapper.selectOne(new LambdaQueryWrapper<Account>()
                    .eq(Account::getObjId, owner.getOwnerId())
                    .last("limit 1"));
        }
        List<Map<String, Object>> history = new ArrayList<>();
        for (OwnerRoomRel rel : rels) {
            if (!"2003".equals(rel.getState())) {
                continue;
            }
            Owner old = ownerMapper.selectById(rel.getOwnerId());
            Map<String, Object> row = new HashMap<>();
            row.put("ownerId", rel.getOwnerId());
            row.put("name", old == null ? rel.getOwnerId() : old.getName());
            row.put("link", old == null ? "" : old.getLink());
            row.put("startTime", rel.getStartTime());
            row.put("endTime", rel.getEndTime());
            history.add(row);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("room", room);
        data.put("unit", unit);
        data.put("floor", floor);
        data.put("owner", owner);
        data.put("account", account);
        data.put("inDate", inDate);
        data.put("historyOwners", history);
        data.put("roomName", roomNameOf(room, floor, unit));
        data.put("balance", account == null || account.getAmount() == null ? BigDecimal.ZERO : account.getAmount());
        return data;
    }

    private String roomNameOf(Room room) {
        Unit unit = unitMapper.selectById(room.getUnitId());
        Floor floor = unit == null ? null : floorMapper.selectById(unit.getFloorId());
        return roomNameOf(room, floor, unit);
    }

    private String roomNameOf(Room room, Floor floor, Unit unit) {
        String floorNum = floor == null ? "?" : floor.getFloorNum();
        String unitNum = unit == null ? "?" : unit.getUnitNum();
        return floorNum + "-" + unitNum + "-" + room.getRoomNum();
    }
}
