package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.OwnerAppUser;
import com.tt.service.OwnerAppUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class OwnerAppUserController {

    @Resource
    private OwnerAppUserService ownerAppUserService;

    @GetMapping("/owner.listAppUserBindingOwners")
    public PageResult<OwnerAppUser> list(@RequestParam(required = false) String communityId,
                                         @RequestParam(required = false) String state,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer row) {
        return ownerAppUserService.listAppUserBindingOwners(communityId, state, page, row);
    }

    @PostMapping("/owner.saveOwnerAppUser")
    public Result<String> save(@RequestBody OwnerAppUser appUser) {
        return Result.ok(ownerAppUserService.saveAuth(appUser));
    }

    @PostMapping("/owner.auditAuthOwner")
    public Result<Void> audit(@RequestBody Map<String, String> body) {
        ownerAppUserService.auditAuthOwner(body.get("appUserId"), body.get("state"), body.get("remark"));
        return Result.ok();
    }
}
