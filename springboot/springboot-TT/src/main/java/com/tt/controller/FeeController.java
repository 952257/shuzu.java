package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.FeeConfig;
import com.tt.po.PayFee;
import com.tt.po.PayFeeDetail;
import com.tt.service.FeeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class FeeController {

    @Resource
    private FeeService feeService;

    @GetMapping("/feeConfig.listFeeConfigs")
    public PageResult<FeeConfig> listConfigs(@RequestParam(required = false) String communityId,
                                             @RequestParam(required = false) String feeName,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer row) {
        return feeService.listFeeConfigs(communityId, feeName, page, row);
    }

    @PostMapping("/feeConfig.saveFeeConfig")
    public Result<String> saveConfig(@RequestBody FeeConfig config) {
        return Result.ok(feeService.saveFeeConfig(config));
    }

    @PostMapping("/feeConfig.updateFeeConfig")
    public Result<Void> updateConfig(@RequestBody FeeConfig config) {
        feeService.updateFeeConfig(config);
        return Result.ok();
    }

    @PostMapping("/feeConfig.deleteFeeConfig")
    public Result<Void> deleteConfig(@RequestBody Map<String, String> body) {
        feeService.deleteFeeConfig(body.get("configId"));
        return Result.ok();
    }

    @GetMapping("/fee.listFee")
    public PageResult<PayFee> listFee(@RequestParam(required = false) String communityId,
                                      @RequestParam(required = false) String payerObjId,
                                      @RequestParam(required = false) String state,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer row) {
        return feeService.listFee(communityId, payerObjId, state, page, row);
    }

    @GetMapping("/fee.queryFee")
    public PageResult<PayFee> queryFee(@RequestParam(required = false) String communityId,
                                       @RequestParam(required = false) String payerObjId,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer row) {
        return feeService.listFee(communityId, payerObjId, null, page, row);
    }

    @PostMapping("/fee.saveFee")
    public Result<String> saveFee(@RequestBody PayFee fee) {
        return Result.ok(feeService.saveFee(fee));
    }

    @PostMapping("/fee.payFee")
    public Result<String> payFee(@RequestBody PayFeeDetail detail) {
        return Result.ok(feeService.payFee(detail));
    }

    @PostMapping("/fee.batchPayFee")
    public Result<Integer> batchPay(@RequestBody Map<String, String> body) {
        return Result.ok(feeService.payFeesByPayer(body.get("payerObjId")));
    }

    @PostMapping("/fee.deleteFee")
    public Result<Void> deleteFee(@RequestBody Map<String, String> body) {
        feeService.deleteFee(body.get("feeId"));
        return Result.ok();
    }

    @PostMapping("/fee.urgeFee")
    public Result<Void> urgeFee(@RequestBody Map<String, String> body) {
        feeService.urgeFee(body.get("feeId"));
        return Result.ok();
    }

    @PostMapping("/fee.refundFee")
    public Result<Void> refundFee(@RequestBody Map<String, String> body) {
        feeService.refundFee(body.get("detailId"), body.get("remark"));
        return Result.ok();
    }

    @PostMapping("/fee.auditFee")
    public Result<Void> auditFee(@RequestBody Map<String, String> body) {
        feeService.auditFee(body.get("detailId"), body.get("auditState"), body.get("remark"));
        return Result.ok();
    }

    @GetMapping("/fee.queryFeeDetail")
    public PageResult<PayFeeDetail> queryFeeDetail(@RequestParam(required = false) String feeId,
                                                   @RequestParam(required = false) String communityId,
                                                   @RequestParam(required = false) String auditState,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer row) {
        return feeService.queryFeeDetail(feeId, communityId, auditState, page, row);
    }

    @GetMapping("/report.feeSummary")
    public Result<Map<String, Object>> feeSummary(@RequestParam(required = false) String communityId) {
        return Result.ok(feeService.feeSummary(communityId));
    }
}
