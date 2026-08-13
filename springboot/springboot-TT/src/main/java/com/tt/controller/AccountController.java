package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Account;
import com.tt.po.AccountDetail;
import com.tt.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("/account.queryOwnerAccount")
    public PageResult<Account> list(@RequestParam(required = false) String communityId,
                                    @RequestParam(required = false) String objId,
                                    @RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer row) {
        return accountService.queryOwnerAccount(communityId, objId, page, row);
    }

    @PostMapping("/account.ownerPrestoreAccount")
    public Result<String> prestore(@RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        return Result.ok(accountService.ownerPrestoreAccount(body.get("acctId"), amount, body.get("remark")));
    }

    @GetMapping("/account.listAccountDetail")
    public PageResult<AccountDetail> details(@RequestParam(required = false) String acctId,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer row) {
        return accountService.listAccountDetail(acctId, page, row);
    }

    @PostMapping("/account.cancelAccountDetail")
    public Result<Void> cancel(@RequestBody Map<String, String> body) {
        accountService.cancelAccountDetail(body.get("detailId"));
        return Result.ok();
    }
}
