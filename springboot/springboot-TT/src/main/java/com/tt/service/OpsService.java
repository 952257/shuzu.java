package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.PhysicalDelete;
import com.tt.common.QueryHelper;
import com.tt.mapper.*;
import com.tt.po.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.function.Consumer;

@Service
public class OpsService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private VoteMapper voteMapper;
    @Resource
    private VisitMapper visitMapper;
    @Resource
    private InspectionMapper inspectionMapper;
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private DiscountMapper discountMapper;
    @Resource
    private PhysicalDelete physicalDelete;

    public PageResult<Org> listOrg(String communityId, String orgName, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Org> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Org::getCommunityId, communityId)
                .like(StringUtils.hasText(orgName), Org::getOrgName, orgName)
                .orderByAsc(Org::getOrgLevel)
                .orderByDesc(Org::getCreateTime);
        return page(orgMapper, wrapper, page, row);
    }

    public String saveOrg(Org org) {
        CommunityGuard.requireCommunity(org.getCommunityId());
        QueryHelper.requireHasText(org.getOrgName(), "部门名称不能为空");
        return saveEntity(org, orgMapper, org::setOrgId, e -> {
            if (!StringUtils.hasText(e.getParentId())) {
                e.setParentId("-1");
            }
            if (!StringUtils.hasText(e.getOrgLevel())) {
                e.setOrgLevel("1");
            }
        });
    }

    public void updateOrg(Org org) {
        QueryHelper.requireHasText(org.getOrgId(), "组织ID不能为空");
        Org db = owned(orgMapper, org.getOrgId(), Org::getCommunityId, "组织不存在");
        org.setCommunityId(db.getCommunityId());
        orgMapper.updateById(org);
    }

    public void deleteOrg(String orgId) {
        QueryHelper.requireHasText(orgId, "组织ID不能为空");
        owned(orgMapper, orgId, Org::getCommunityId, "组织不存在");
        physicalDelete.byId(Org.class, orgId);
    }

    public PageResult<Notice> listNotice(String communityId, String title, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getCommunityId, communityId)
                .like(StringUtils.hasText(title), Notice::getTitle, title)
                .eq(StringUtils.hasText(state), Notice::getState, state)
                .orderByDesc(Notice::getCreateTime);
        return page(noticeMapper, wrapper, page, row);
    }

    public String saveNotice(Notice notice) {
        CommunityGuard.requireCommunity(notice.getCommunityId());
        QueryHelper.requireHasText(notice.getTitle(), "标题不能为空");
        return saveEntity(notice, noticeMapper, notice::setNoticeId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("1000");
            }
        });
    }

    public void updateNotice(Notice notice) {
        QueryHelper.requireHasText(notice.getNoticeId(), "公告ID不能为空");
        Notice db = owned(noticeMapper, notice.getNoticeId(), Notice::getCommunityId, "公告不存在");
        notice.setCommunityId(db.getCommunityId());
        noticeMapper.updateById(notice);
    }

    public void deleteNotice(String noticeId) {
        QueryHelper.requireHasText(noticeId, "公告ID不能为空");
        owned(noticeMapper, noticeId, Notice::getCommunityId, "公告不存在");
        physicalDelete.byId(Notice.class, noticeId);
    }

    public void publishNotice(String noticeId) {
        Notice notice = owned(noticeMapper, noticeId, Notice::getCommunityId, "公告不存在");
        notice.setState("2000");
        noticeMapper.updateById(notice);
    }

    public PageResult<Vote> listVote(String communityId, String title, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Vote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vote::getCommunityId, communityId)
                .like(StringUtils.hasText(title), Vote::getTitle, title)
                .eq(StringUtils.hasText(state), Vote::getState, state)
                .orderByDesc(Vote::getCreateTime);
        return page(voteMapper, wrapper, page, row);
    }

    public String saveVote(Vote vote) {
        CommunityGuard.requireCommunity(vote.getCommunityId());
        QueryHelper.requireHasText(vote.getTitle(), "标题不能为空");
        return saveEntity(vote, voteMapper, vote::setVoteId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("2000");
            }
        });
    }

    public void updateVote(Vote vote) {
        QueryHelper.requireHasText(vote.getVoteId(), "问卷ID不能为空");
        Vote db = owned(voteMapper, vote.getVoteId(), Vote::getCommunityId, "问卷不存在");
        vote.setCommunityId(db.getCommunityId());
        voteMapper.updateById(vote);
    }

    public void deleteVote(String voteId) {
        QueryHelper.requireHasText(voteId, "问卷ID不能为空");
        owned(voteMapper, voteId, Vote::getCommunityId, "问卷不存在");
        physicalDelete.byId(Vote.class, voteId);
    }

    public void finishVote(String voteId) {
        Vote vote = owned(voteMapper, voteId, Vote::getCommunityId, "问卷不存在");
        vote.setState("3000");
        voteMapper.updateById(vote);
    }

    public PageResult<Visit> listVisit(String communityId, String name, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Visit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Visit::getCommunityId, communityId)
                .like(StringUtils.hasText(name), Visit::getName, name)
                .eq(StringUtils.hasText(state), Visit::getState, state)
                .orderByDesc(Visit::getCreateTime);
        return page(visitMapper, wrapper, page, row);
    }

    public String saveVisit(Visit visit) {
        CommunityGuard.requireCommunity(visit.getCommunityId());
        QueryHelper.requireHasText(visit.getName(), "访客姓名不能为空");
        return saveEntity(visit, visitMapper, visit::setVisitId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("1000");
            }
            if (e.getVisitTime() == null) {
                e.setVisitTime(new Date());
            }
        });
    }

    public void updateVisit(Visit visit) {
        QueryHelper.requireHasText(visit.getVisitId(), "访客ID不能为空");
        Visit db = owned(visitMapper, visit.getVisitId(), Visit::getCommunityId, "访客记录不存在");
        visit.setCommunityId(db.getCommunityId());
        visitMapper.updateById(visit);
    }

    public void deleteVisit(String visitId) {
        QueryHelper.requireHasText(visitId, "访客ID不能为空");
        owned(visitMapper, visitId, Visit::getCommunityId, "访客记录不存在");
        physicalDelete.byId(Visit.class, visitId);
    }

    public void arriveVisit(String visitId) {
        Visit visit = owned(visitMapper, visitId, Visit::getCommunityId, "访客记录不存在");
        visit.setState("2000");
        visit.setVisitTime(new Date());
        visitMapper.updateById(visit);
    }

    public void leaveVisit(String visitId) {
        Visit visit = owned(visitMapper, visitId, Visit::getCommunityId, "访客记录不存在");
        visit.setState("3000");
        visit.setDepartureTime(new Date());
        visitMapper.updateById(visit);
    }

    public PageResult<Inspection> listInspection(String communityId, String planName, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Inspection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inspection::getCommunityId, communityId)
                .like(StringUtils.hasText(planName), Inspection::getPlanName, planName)
                .eq(StringUtils.hasText(state), Inspection::getState, state)
                .orderByDesc(Inspection::getCreateTime);
        return page(inspectionMapper, wrapper, page, row);
    }

    public String saveInspection(Inspection inspection) {
        CommunityGuard.requireCommunity(inspection.getCommunityId());
        QueryHelper.requireHasText(inspection.getPlanName(), "巡检计划不能为空");
        return saveEntity(inspection, inspectionMapper, inspection::setTaskId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("1000");
            }
        });
    }

    public void updateInspection(Inspection inspection) {
        QueryHelper.requireHasText(inspection.getTaskId(), "巡检任务ID不能为空");
        Inspection db = owned(inspectionMapper, inspection.getTaskId(), Inspection::getCommunityId, "巡检任务不存在");
        inspection.setCommunityId(db.getCommunityId());
        inspectionMapper.updateById(inspection);
    }

    public void deleteInspection(String taskId) {
        QueryHelper.requireHasText(taskId, "巡检任务ID不能为空");
        owned(inspectionMapper, taskId, Inspection::getCommunityId, "巡检任务不存在");
        physicalDelete.byId(Inspection.class, taskId);
    }

    public void finishInspection(String taskId, String staffName, String remark) {
        Inspection task = owned(inspectionMapper, taskId, Inspection::getCommunityId, "巡检任务不存在");
        task.setState("2000");
        if (StringUtils.hasText(staffName)) {
            task.setStaffName(staffName);
        }
        if (StringUtils.hasText(remark)) {
            task.setRemark(remark);
        }
        task.setInspectTime(new Date());
        inspectionMapper.updateById(task);
    }

    public PageResult<Purchase> listPurchase(String communityId, String resourceName, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Purchase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Purchase::getCommunityId, communityId)
                .like(StringUtils.hasText(resourceName), Purchase::getResourceName, resourceName)
                .eq(StringUtils.hasText(state), Purchase::getState, state)
                .orderByDesc(Purchase::getCreateTime);
        return page(purchaseMapper, wrapper, page, row);
    }

    public String savePurchase(Purchase purchase) {
        CommunityGuard.requireCommunity(purchase.getCommunityId());
        QueryHelper.requireHasText(purchase.getResourceName(), "物品名称不能为空");
        return saveEntity(purchase, purchaseMapper, purchase::setApplyId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("1000");
            }
        });
    }

    public void updatePurchase(Purchase purchase) {
        QueryHelper.requireHasText(purchase.getApplyId(), "采购单ID不能为空");
        Purchase db = owned(purchaseMapper, purchase.getApplyId(), Purchase::getCommunityId, "采购单不存在");
        purchase.setCommunityId(db.getCommunityId());
        purchaseMapper.updateById(purchase);
    }

    public void deletePurchase(String applyId) {
        QueryHelper.requireHasText(applyId, "采购单ID不能为空");
        owned(purchaseMapper, applyId, Purchase::getCommunityId, "采购单不存在");
        physicalDelete.byId(Purchase.class, applyId);
    }

    public void auditPurchase(String applyId, String state) {
        Purchase purchase = owned(purchaseMapper, applyId, Purchase::getCommunityId, "采购单不存在");
        QueryHelper.requireHasText(state, "审核状态不能为空");
        purchase.setState(state);
        purchaseMapper.updateById(purchase);
    }

    public PageResult<Contract> listContract(String communityId, String contractName, String state, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contract::getCommunityId, communityId)
                .like(StringUtils.hasText(contractName), Contract::getContractName, contractName)
                .eq(StringUtils.hasText(state), Contract::getState, state)
                .orderByDesc(Contract::getCreateTime);
        return page(contractMapper, wrapper, page, row);
    }

    public String saveContract(Contract contract) {
        CommunityGuard.requireCommunity(contract.getCommunityId());
        QueryHelper.requireHasText(contract.getContractName(), "合同名称不能为空");
        QueryHelper.requireHasText(contract.getContractCode(), "合同编号不能为空");
        return saveEntity(contract, contractMapper, contract::setContractId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("2000");
            }
        });
    }

    public void updateContract(Contract contract) {
        QueryHelper.requireHasText(contract.getContractId(), "合同ID不能为空");
        Contract db = owned(contractMapper, contract.getContractId(), Contract::getCommunityId, "合同不存在");
        contract.setCommunityId(db.getCommunityId());
        contractMapper.updateById(contract);
    }

    public void deleteContract(String contractId) {
        QueryHelper.requireHasText(contractId, "合同ID不能为空");
        owned(contractMapper, contractId, Contract::getCommunityId, "合同不存在");
        physicalDelete.byId(Contract.class, contractId);
    }

    public PageResult<Discount> listDiscount(String communityId, String discountName, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Discount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Discount::getCommunityId, communityId)
                .like(StringUtils.hasText(discountName), Discount::getDiscountName, discountName)
                .orderByDesc(Discount::getCreateTime);
        return page(discountMapper, wrapper, page, row);
    }

    public String saveDiscount(Discount discount) {
        CommunityGuard.requireCommunity(discount.getCommunityId());
        QueryHelper.requireHasText(discount.getDiscountName(), "折扣名称不能为空");
        return saveEntity(discount, discountMapper, discount::setDiscountId, e -> {
            if (!StringUtils.hasText(e.getState())) {
                e.setState("1000");
            }
        });
    }

    public void updateDiscount(Discount discount) {
        QueryHelper.requireHasText(discount.getDiscountId(), "折扣ID不能为空");
        Discount db = owned(discountMapper, discount.getDiscountId(), Discount::getCommunityId, "折扣不存在");
        discount.setCommunityId(db.getCommunityId());
        discountMapper.updateById(discount);
    }

    public void deleteDiscount(String discountId) {
        QueryHelper.requireHasText(discountId, "折扣ID不能为空");
        owned(discountMapper, discountId, Discount::getCommunityId, "折扣不存在");
        physicalDelete.byId(Discount.class, discountId);
    }

    private <T> T owned(BaseMapper<T> mapper, String id, java.util.function.Function<T, String> communityId, String notFound) {
        return CommunityGuard.mustBelong(mapper.selectById(id), communityId, notFound);
    }

    private <T> String saveEntity(T entity, BaseMapper<T> mapper, Consumer<String> setId, Consumer<T> beforeSave) {
        String id = IdGenerator.nextId();
        setId.accept(id);
        if (beforeSave != null) {
            beforeSave.accept(entity);
        }
        mapper.insert(entity);
        return id;
    }

    private <T> PageResult<T> page(BaseMapper<T> mapper, LambdaQueryWrapper<T> wrapper, Integer page, Integer row) {
        int p = QueryHelper.page(page);
        int r = QueryHelper.row(row);
        Page<T> mp = mapper.selectPage(new Page<>(p, r), wrapper);
        return PageResult.of(mp.getRecords(), mp.getTotal(), p, r);
    }
}
