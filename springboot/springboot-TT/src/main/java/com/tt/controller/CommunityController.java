package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Community;
import com.tt.service.CommunityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/community.listCommunitys")
    public PageResult<Community> list(@RequestParam(required = false) String communityId,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String cityCode,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer row) {
        return communityService.listCommunitys(communityId, name, cityCode, page, row);
    }

    @PostMapping("/community.saveCommunity")
    public Result<String> save(@RequestBody Community community) {
        return Result.ok(communityService.saveCommunity(community));
    }

    @PostMapping("/community.updateCommunity")
    public Result<Void> update(@RequestBody Community community) {
        communityService.updateCommunity(community);
        return Result.ok();
    }

    @PostMapping("/community.deleteCommunity")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        communityService.deleteCommunity(body.get("communityId"));
        return Result.ok();
    }
}
