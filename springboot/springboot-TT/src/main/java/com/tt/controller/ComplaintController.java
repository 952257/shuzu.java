package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Complaint;
import com.tt.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class ComplaintController {

    @Resource
    private ComplaintService complaintService;

    @GetMapping("/complaint.listComplaints")
    public PageResult<Complaint> list(@RequestParam(required = false) String communityId,
                                      @RequestParam(required = false) String typeCd,
                                      @RequestParam(required = false) String state,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer row) {
        return complaintService.listComplaints(communityId, typeCd, state, page, row);
    }

    @PostMapping("/complaint.saveComplaint")
    public Result<String> save(@RequestBody Complaint complaint) {
        return Result.ok(complaintService.saveComplaint(complaint));
    }

    @PostMapping("/complaint.updateComplaint")
    public Result<Void> update(@RequestBody Complaint complaint) {
        complaintService.updateComplaint(complaint);
        return Result.ok();
    }

    @PostMapping("/complaint.deleteComplaint")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        complaintService.deleteComplaint(body.get("complaintId"));
        return Result.ok();
    }

    @PostMapping("/complaint.auditComplaint")
    public Result<Void> audit(@RequestBody Map<String, String> body) {
        complaintService.auditComplaint(body.get("complaintId"));
        return Result.ok();
    }
}
