package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.CommunitySetting;
import com.tt.po.UserLogin;
import com.tt.service.BizDeskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class BizDeskController {

    @Resource
    private BizDeskService bizDeskService;

    @GetMapping("/room.queryRoomByCode")
    public Result<Map<String, Object>> byCode(@RequestParam(required = false) String communityId,
                                              @RequestParam String roomCode) {
        return Result.ok(bizDeskService.queryRoomByCode(communityId, roomCode));
    }

    @GetMapping("/owner.queryOwnerByRoom")
    public Result<Map<String, Object>> byRoom(@RequestParam String roomId) {
        return Result.ok(bizDeskService.queryDeskByRoomId(roomId));
    }

    @GetMapping("/room.listRoomOptions")
    public Result<List<Map<String, Object>>> options(@RequestParam(required = false) String communityId) {
        return Result.ok(bizDeskService.listRoomOptions(communityId));
    }

    @PostMapping("/user.changePassword")
    public Result<Void> changePassword(@RequestBody Map<String, String> body) {
        bizDeskService.changePassword(body.get("oldPass"), body.get("newPass"));
        return Result.ok();
    }

    @GetMapping("/user.listLoginLogs")
    public PageResult<UserLogin> logs(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer row) {
        return bizDeskService.listLoginLogs(page, row);
    }

    @GetMapping("/communitySetting.listSettings")
    public Result<List<CommunitySetting>> listSettings(@RequestParam(required = false) String communityId,
                                                       @RequestParam(required = false) String settingGroup) {
        return Result.ok(bizDeskService.listSettings(communityId, settingGroup));
    }

    @PostMapping("/communitySetting.saveSettings")
    public Result<Void> saveSettings(@RequestBody Map<String, Object> body) {
        String communityId = body.get("communityId") == null ? null : String.valueOf(body.get("communityId"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> raw = (List<Map<String, Object>>) body.get("settings");
        List<CommunitySetting> settings = new java.util.ArrayList<>();
        if (raw != null) {
            for (Map<String, Object> item : raw) {
                CommunitySetting setting = new CommunitySetting();
                setting.setSettingKey(str(item.get("settingKey")));
                setting.setSettingName(str(item.get("settingName")));
                setting.setSettingValue(str(item.get("settingValue")));
                setting.setSettingGroup(str(item.get("settingGroup")));
                setting.setRemark(str(item.get("remark")));
                settings.add(setting);
            }
        }
        bizDeskService.saveSettings(communityId, settings);
        return Result.ok();
    }

    private String str(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
