package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.ComplaintMapper;
import com.tt.po.Complaint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ComplaintService extends PhysicalServiceImpl<ComplaintMapper, Complaint> {

    public PageResult<Complaint> listComplaints(String communityId, String typeCd, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Complaint::getCommunityId, communityId)
                .eq(StringUtils.hasText(typeCd), Complaint::getTypeCd, typeCd)
                .eq(StringUtils.hasText(state), Complaint::getState, state)
                .orderByDesc(Complaint::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveComplaint(Complaint complaint) {
        CommunityGuard.requireCommunity(complaint.getCommunityId());
        QueryHelper.requireHasText(complaint.getContext(), "投诉内容不能为空");
        complaint.setComplaintId(IdGenerator.nextId());
        if (!StringUtils.hasText(complaint.getState())) {
            complaint.setState("10001");
        }
        if (!StringUtils.hasText(complaint.getTypeCd())) {
            complaint.setTypeCd("809001");
        }
        save(complaint);
        return complaint.getComplaintId();
    }

    public void updateComplaint(Complaint complaint) {
        QueryHelper.requireHasText(complaint.getComplaintId(), "投诉ID不能为空");
        Complaint db = getById(complaint.getComplaintId());
        CommunityGuard.mustBelong(db, Complaint::getCommunityId, "投诉不存在");
        complaint.setCommunityId(db.getCommunityId());
        updateById(complaint);
    }

    public void deleteComplaint(String complaintId) {
        QueryHelper.requireHasText(complaintId, "投诉ID不能为空");
        Complaint db = getById(complaintId);
        CommunityGuard.mustBelong(db, Complaint::getCommunityId, "投诉不存在");
        removeById(complaintId);
    }

    public void auditComplaint(String complaintId) {
        Complaint complaint = getById(complaintId);
        CommunityGuard.mustBelong(complaint, Complaint::getCommunityId, "投诉不存在");
        complaint.setState("10003");
        complaint.setCurrentUserId(UserContext.getUserId());
        complaint.setCurrentUserName(UserContext.getUserName());
        updateById(complaint);
    }
}
