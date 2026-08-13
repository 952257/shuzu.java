package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Owner;
import com.tt.service.OwnerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    @GetMapping("/owner.queryOwners")
    public PageResult<Owner> list(@RequestParam(required = false) String communityId,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String link,
                                  @RequestParam(required = false) String ownerTypeCd,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer row) {
        return ownerService.queryOwners(communityId, name, link, ownerTypeCd, page, row);
    }

    @GetMapping("/owner.queryOwnerMembers")
    public PageResult<Owner> members(@RequestParam String ownerId,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer row) {
        return ownerService.queryOwnerMembers(ownerId, page, row);
    }

    @PostMapping("/owner.saveOwner")
    public Result<String> save(@RequestBody Owner owner) {
        return Result.ok(ownerService.saveOwner(owner));
    }

    @PutMapping("/owner.editOwner")
    public Result<Void> edit(@RequestBody Owner owner) {
        ownerService.editOwner(owner);
        return Result.ok();
    }

    @DeleteMapping("/owner.deleteOwner")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        ownerService.deleteOwner(body.get("memberId"));
        return Result.ok();
    }
}
