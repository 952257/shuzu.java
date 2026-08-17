package com.tt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.CommunityGuard;
import com.tt.common.Result;
import com.tt.common.UserContext;
import com.tt.mapper.*;
import com.tt.po.Community;
import com.tt.po.Complaint;
import com.tt.po.Inspection;
import com.tt.po.Notice;
import com.tt.po.Owner;
import com.tt.po.PayFee;
import com.tt.po.Repair;
import com.tt.po.Room;
import com.tt.po.Visit;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class DashboardController {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private OwnerMapper ownerMapper;
    @Resource
    private PayFeeMapper payFeeMapper;
    @Resource
    private RepairMapper repairMapper;
    @Resource
    private ComplaintMapper complaintMapper;
    @Resource
    private VisitMapper visitMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private InspectionMapper inspectionMapper;

    @GetMapping("/dashboard.stats")
    public Result<Map<String, Long>> stats(@RequestParam String communityId) {
        CommunityGuard.requireCommunity(communityId);
        String storeId = UserContext.getStoreId();
        Map<String, Long> data = new HashMap<>();
        data.put("communityCount", communityMapper.selectCount(new LambdaQueryWrapper<Community>()
                .eq(StringUtils.hasText(storeId), Community::getStoreId, storeId)));
        data.put("roomCount", roomMapper.selectCount(new LambdaQueryWrapper<Room>().eq(Room::getCommunityId, communityId)));
        data.put("ownerCount", ownerMapper.selectCount(new LambdaQueryWrapper<Owner>().eq(Owner::getCommunityId, communityId)));
        data.put("feeCount", payFeeMapper.selectCount(new LambdaQueryWrapper<PayFee>().eq(PayFee::getCommunityId, communityId)));
        data.put("repairCount", repairMapper.selectCount(new LambdaQueryWrapper<Repair>().eq(Repair::getCommunityId, communityId)));
        data.put("complaintCount", complaintMapper.selectCount(new LambdaQueryWrapper<Complaint>().eq(Complaint::getCommunityId, communityId)));
        data.put("visitCount", visitMapper.selectCount(new LambdaQueryWrapper<Visit>().eq(Visit::getCommunityId, communityId)));
        data.put("noticeCount", noticeMapper.selectCount(new LambdaQueryWrapper<Notice>().eq(Notice::getCommunityId, communityId)));
        data.put("inspectionCount", inspectionMapper.selectCount(new LambdaQueryWrapper<Inspection>().eq(Inspection::getCommunityId, communityId)));
        return Result.ok(data);
    }

    @GetMapping("/report.workSummary")
    public Result<Map<String, Object>> workSummary(@RequestParam String communityId) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Repair> repairWrapper = new LambdaQueryWrapper<>();
        repairWrapper.eq(Repair::getCommunityId, communityId);
        Map<String, Long> repair = new HashMap<>();
        repair.put("total", repairMapper.selectCount(repairWrapper));
        repair.put("pending", repairMapper.selectCount(cloneRepair(communityId, "1000")));
        repair.put("processing", repairMapper.selectCount(cloneRepair(communityId, "1100")));
        repair.put("done", repairMapper.selectCount(cloneRepair(communityId, "1200")));

        LambdaQueryWrapper<Complaint> complaintWrapper = new LambdaQueryWrapper<>();
        complaintWrapper.eq(Complaint::getCommunityId, communityId);
        Map<String, Long> complaint = new HashMap<>();
        complaint.put("total", complaintMapper.selectCount(complaintWrapper));
        complaint.put("pending", complaintMapper.selectCount(cloneComplaint(communityId, "10001")));
        complaint.put("processing", complaintMapper.selectCount(cloneComplaint(communityId, "10002")));
        complaint.put("done", complaintMapper.selectCount(cloneComplaint(communityId, "10003")));

        Map<String, Object> data = new HashMap<>();
        data.put("repair", repair);
        data.put("complaint", complaint);
        return Result.ok(data);
    }

    private LambdaQueryWrapper<Repair> cloneRepair(String communityId, String state) {
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Repair::getCommunityId, communityId)
                .eq(Repair::getState, state);
        return wrapper;
    }

    private LambdaQueryWrapper<Complaint> cloneComplaint(String communityId, String state) {
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Complaint::getCommunityId, communityId)
                .eq(Complaint::getState, state);
        return wrapper;
    }
}
