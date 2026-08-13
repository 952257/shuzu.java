package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.*;
import com.tt.service.OpsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class OpsController {

    @Resource
    private OpsService opsService;

    @GetMapping("/org.listOrgs")
    public PageResult<Org> listOrg(@RequestParam(required = false) String communityId,
                                   @RequestParam(required = false) String orgName,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer row) {
        return opsService.listOrg(communityId, orgName, page, row);
    }

    @PostMapping("/org.saveOrg")
    public Result<String> saveOrg(@RequestBody Org org) {
        return Result.ok(opsService.saveOrg(org));
    }

    @PutMapping("/org.updateOrg")
    public Result<Void> updateOrg(@RequestBody Org org) {
        opsService.updateOrg(org);
        return Result.ok();
    }

    @DeleteMapping("/org.deleteOrg")
    public Result<Void> deleteOrg(@RequestBody Map<String, String> body) {
        opsService.deleteOrg(body.get("orgId"));
        return Result.ok();
    }

    @GetMapping("/notice.listNotices")
    public PageResult<Notice> listNotice(@RequestParam(required = false) String communityId,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) String state,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer row) {
        return opsService.listNotice(communityId, title, state, page, row);
    }

    @PostMapping("/notice.saveNotice")
    public Result<String> saveNotice(@RequestBody Notice notice) {
        return Result.ok(opsService.saveNotice(notice));
    }

    @PutMapping("/notice.updateNotice")
    public Result<Void> updateNotice(@RequestBody Notice notice) {
        opsService.updateNotice(notice);
        return Result.ok();
    }

    @DeleteMapping("/notice.deleteNotice")
    public Result<Void> deleteNotice(@RequestBody Map<String, String> body) {
        opsService.deleteNotice(body.get("noticeId"));
        return Result.ok();
    }

    @PostMapping("/notice.publishNotice")
    public Result<Void> publishNotice(@RequestBody Map<String, String> body) {
        opsService.publishNotice(body.get("noticeId"));
        return Result.ok();
    }

    @GetMapping("/vote.listVotes")
    public PageResult<Vote> listVote(@RequestParam(required = false) String communityId,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String state,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer row) {
        return opsService.listVote(communityId, title, state, page, row);
    }

    @PostMapping("/vote.saveVote")
    public Result<String> saveVote(@RequestBody Vote vote) {
        return Result.ok(opsService.saveVote(vote));
    }

    @PutMapping("/vote.updateVote")
    public Result<Void> updateVote(@RequestBody Vote vote) {
        opsService.updateVote(vote);
        return Result.ok();
    }

    @DeleteMapping("/vote.deleteVote")
    public Result<Void> deleteVote(@RequestBody Map<String, String> body) {
        opsService.deleteVote(body.get("voteId"));
        return Result.ok();
    }

    @PostMapping("/vote.finishVote")
    public Result<Void> finishVote(@RequestBody Map<String, String> body) {
        opsService.finishVote(body.get("voteId"));
        return Result.ok();
    }

    @GetMapping("/visit.listVisits")
    public PageResult<Visit> listVisit(@RequestParam(required = false) String communityId,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String state,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer row) {
        return opsService.listVisit(communityId, name, state, page, row);
    }

    @PostMapping("/visit.saveVisit")
    public Result<String> saveVisit(@RequestBody Visit visit) {
        return Result.ok(opsService.saveVisit(visit));
    }

    @PutMapping("/visit.updateVisit")
    public Result<Void> updateVisit(@RequestBody Visit visit) {
        opsService.updateVisit(visit);
        return Result.ok();
    }

    @DeleteMapping("/visit.deleteVisit")
    public Result<Void> deleteVisit(@RequestBody Map<String, String> body) {
        opsService.deleteVisit(body.get("visitId"));
        return Result.ok();
    }

    @PostMapping("/visit.arriveVisit")
    public Result<Void> arriveVisit(@RequestBody Map<String, String> body) {
        opsService.arriveVisit(body.get("visitId"));
        return Result.ok();
    }

    @PostMapping("/visit.leaveVisit")
    public Result<Void> leaveVisit(@RequestBody Map<String, String> body) {
        opsService.leaveVisit(body.get("visitId"));
        return Result.ok();
    }

    @GetMapping("/inspection.listInspections")
    public PageResult<Inspection> listInspection(@RequestParam(required = false) String communityId,
                                                 @RequestParam(required = false) String planName,
                                                 @RequestParam(required = false) String state,
                                                 @RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer row) {
        return opsService.listInspection(communityId, planName, state, page, row);
    }

    @PostMapping("/inspection.saveInspection")
    public Result<String> saveInspection(@RequestBody Inspection inspection) {
        return Result.ok(opsService.saveInspection(inspection));
    }

    @PutMapping("/inspection.updateInspection")
    public Result<Void> updateInspection(@RequestBody Inspection inspection) {
        opsService.updateInspection(inspection);
        return Result.ok();
    }

    @DeleteMapping("/inspection.deleteInspection")
    public Result<Void> deleteInspection(@RequestBody Map<String, String> body) {
        opsService.deleteInspection(body.get("taskId"));
        return Result.ok();
    }

    @PostMapping("/inspection.finishInspection")
    public Result<Void> finishInspection(@RequestBody Map<String, String> body) {
        opsService.finishInspection(body.get("taskId"), body.get("staffName"), body.get("remark"));
        return Result.ok();
    }

    @GetMapping("/purchase.listPurchases")
    public PageResult<Purchase> listPurchase(@RequestParam(required = false) String communityId,
                                             @RequestParam(required = false) String resourceName,
                                             @RequestParam(required = false) String state,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer row) {
        return opsService.listPurchase(communityId, resourceName, state, page, row);
    }

    @PostMapping("/purchase.savePurchase")
    public Result<String> savePurchase(@RequestBody Purchase purchase) {
        return Result.ok(opsService.savePurchase(purchase));
    }

    @PutMapping("/purchase.updatePurchase")
    public Result<Void> updatePurchase(@RequestBody Purchase purchase) {
        opsService.updatePurchase(purchase);
        return Result.ok();
    }

    @DeleteMapping("/purchase.deletePurchase")
    public Result<Void> deletePurchase(@RequestBody Map<String, String> body) {
        opsService.deletePurchase(body.get("applyId"));
        return Result.ok();
    }

    @PostMapping("/purchase.auditPurchase")
    public Result<Void> auditPurchase(@RequestBody Map<String, String> body) {
        opsService.auditPurchase(body.get("applyId"), body.get("state"));
        return Result.ok();
    }

    @GetMapping("/contract.listContracts")
    public PageResult<Contract> listContract(@RequestParam(required = false) String communityId,
                                             @RequestParam(required = false) String contractName,
                                             @RequestParam(required = false) String state,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer row) {
        return opsService.listContract(communityId, contractName, state, page, row);
    }

    @PostMapping("/contract.saveContract")
    public Result<String> saveContract(@RequestBody Contract contract) {
        return Result.ok(opsService.saveContract(contract));
    }

    @PutMapping("/contract.updateContract")
    public Result<Void> updateContract(@RequestBody Contract contract) {
        opsService.updateContract(contract);
        return Result.ok();
    }

    @DeleteMapping("/contract.deleteContract")
    public Result<Void> deleteContract(@RequestBody Map<String, String> body) {
        opsService.deleteContract(body.get("contractId"));
        return Result.ok();
    }

    @GetMapping("/feeDiscount.listDiscounts")
    public PageResult<Discount> listDiscount(@RequestParam(required = false) String communityId,
                                             @RequestParam(required = false) String discountName,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer row) {
        return opsService.listDiscount(communityId, discountName, page, row);
    }

    @PostMapping("/feeDiscount.saveDiscount")
    public Result<String> saveDiscount(@RequestBody Discount discount) {
        return Result.ok(opsService.saveDiscount(discount));
    }

    @PutMapping("/feeDiscount.updateDiscount")
    public Result<Void> updateDiscount(@RequestBody Discount discount) {
        opsService.updateDiscount(discount);
        return Result.ok();
    }

    @DeleteMapping("/feeDiscount.deleteDiscount")
    public Result<Void> deleteDiscount(@RequestBody Map<String, String> body) {
        opsService.deleteDiscount(body.get("discountId"));
        return Result.ok();
    }
}
