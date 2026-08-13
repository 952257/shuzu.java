package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.User;
import com.tt.service.StaffService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class StaffController {

    @Resource
    private StaffService staffService;

    @GetMapping("/query.staff.infos")
    public PageResult<User> list(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String tel,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer row) {
        return staffService.listStaff(name, tel, page, row);
    }

    @PostMapping("/user.staff.add")
    public Result<String> add(@RequestBody User user) {
        return Result.ok(staffService.addStaff(user));
    }

    @PostMapping("/user.staff.modify")
    public Result<Void> modify(@RequestBody User user) {
        staffService.modifyStaff(user);
        return Result.ok();
    }

    @PostMapping("/user.staff.delete")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        staffService.deleteStaff(body.get("userId"));
        return Result.ok();
    }
}
