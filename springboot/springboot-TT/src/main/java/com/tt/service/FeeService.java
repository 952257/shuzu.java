package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.mapper.FeeConfigMapper;
import com.tt.mapper.PayFeeDetailMapper;
import com.tt.mapper.PayFeeMapper;
import com.tt.po.FeeConfig;
import com.tt.po.PayFee;
import com.tt.po.PayFeeDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeeService extends ServiceImpl<PayFeeMapper, PayFee> {

    @Resource
    private FeeConfigMapper feeConfigMapper;
    @Resource
    private PayFeeDetailMapper payFeeDetailMapper;
    @Resource
    private FeeConfigService feeConfigService;

    public PageResult<FeeConfig> listFeeConfigs(String communityId, String feeName, Integer page, Integer row) {
        LambdaQueryWrapper<FeeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), FeeConfig::getCommunityId, communityId)
                .like(StringUtils.hasText(feeName), FeeConfig::getFeeName, feeName)
                .orderByDesc(FeeConfig::getCreateTime);
        return QueryHelper.toPage(feeConfigService, wrapper, page, row);
    }

    public String saveFeeConfig(FeeConfig config) {
        QueryHelper.requireHasText(config.getCommunityId(), "小区ID不能为空");
        QueryHelper.requireHasText(config.getFeeName(), "费用项名称不能为空");
        QueryHelper.requireHasText(config.getFeeTypeCd(), "费用类型不能为空");
        config.setConfigId(IdGenerator.nextId());
        feeConfigMapper.insert(config);
        return config.getConfigId();
    }

    public void updateFeeConfig(FeeConfig config) {
        QueryHelper.requireHasText(config.getConfigId(), "费用项ID不能为空");
        feeConfigMapper.updateById(config);
    }

    public void deleteFeeConfig(String configId) {
        QueryHelper.requireHasText(configId, "费用项ID不能为空");
        feeConfigMapper.deleteById(configId);
    }

    public PageResult<PayFee> listFee(String communityId, String payerObjId, String state, Integer page, Integer row) {
        LambdaQueryWrapper<PayFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(communityId), PayFee::getCommunityId, communityId)
                .eq(StringUtils.hasText(payerObjId), PayFee::getPayerObjId, payerObjId)
                .eq(StringUtils.hasText(state), PayFee::getState, state)
                .orderByDesc(PayFee::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveFee(PayFee fee) {
        QueryHelper.requireHasText(fee.getConfigId(), "费用项ID不能为空");
        QueryHelper.requireHasText(fee.getPayerObjId(), "缴费对象不能为空");
        QueryHelper.requireHasText(fee.getCommunityId(), "小区ID不能为空");
        FeeConfig config = feeConfigMapper.selectById(fee.getConfigId());
        QueryHelper.require(config != null, "费用项不存在");
        fee.setFeeId(IdGenerator.nextId());
        fee.setFeeName(config.getFeeName());
        if (!StringUtils.hasText(fee.getState())) {
            fee.setState("2008001");
        }
        save(fee);
        return fee.getFeeId();
    }

    @Transactional
    public String payFee(PayFeeDetail detail) {
        QueryHelper.requireHasText(detail.getFeeId(), "费用ID不能为空");
        PayFee fee = getById(detail.getFeeId());
        QueryHelper.require(fee != null, "费用不存在");
        detail.setDetailId(IdGenerator.nextId());
        detail.setCommunityId(fee.getCommunityId());
        detail.setPayTime(new Date());
        if (detail.getReceivedAmount() == null) {
            detail.setReceivedAmount(fee.getAmount() == null ? BigDecimal.ZERO : fee.getAmount());
        }
        if (detail.getReceivableAmount() == null) {
            detail.setReceivableAmount(detail.getReceivedAmount());
        }
        if (!StringUtils.hasText(detail.getState())) {
            detail.setState("1400");
        }
        if (!StringUtils.hasText(detail.getAuditState())) {
            detail.setAuditState("1000");
        }
        payFeeDetailMapper.insert(detail);
        return detail.getDetailId();
    }

    @Transactional
    public int payFeesByPayer(String payerObjId) {
        QueryHelper.requireHasText(payerObjId, "缴费对象不能为空");
        List<PayFee> fees = list(new LambdaQueryWrapper<PayFee>()
                .eq(PayFee::getPayerObjId, payerObjId)
                .eq(PayFee::getState, "2008001"));
        for (PayFee fee : fees) {
            PayFeeDetail detail = new PayFeeDetail();
            detail.setFeeId(fee.getFeeId());
            payFee(detail);
        }
        return fees.size();
    }

    public void urgeFee(String feeId) {
        QueryHelper.requireHasText(feeId, "费用ID不能为空");
        PayFee fee = getById(feeId);
        QueryHelper.require(fee != null, "费用不存在");
        QueryHelper.require("2008001".equals(fee.getState()), "仅收费中的账单可催缴");
    }

    @Transactional
    public void refundFee(String detailId, String remark) {
        QueryHelper.requireHasText(detailId, "缴费记录ID不能为空");
        PayFeeDetail detail = payFeeDetailMapper.selectById(detailId);
        QueryHelper.require(detail != null, "缴费记录不存在");
        QueryHelper.require(!"1500".equals(detail.getState()), "该记录已退费");
        QueryHelper.require("1100".equals(detail.getAuditState()) || !StringUtils.hasText(detail.getAuditState()), "仅已审核通过的缴费可退费");
        detail.setState("1500");
        if (StringUtils.hasText(remark)) {
            detail.setRemark(remark);
        }
        payFeeDetailMapper.updateById(detail);
    }

    public void auditFee(String detailId, String auditState, String remark) {
        QueryHelper.requireHasText(detailId, "缴费记录ID不能为空");
        QueryHelper.requireHasText(auditState, "审核结果不能为空");
        PayFeeDetail detail = payFeeDetailMapper.selectById(detailId);
        QueryHelper.require(detail != null, "缴费记录不存在");
        QueryHelper.require("1000".equals(detail.getAuditState()) || !StringUtils.hasText(detail.getAuditState()), "该记录已审核");
        detail.setAuditState(auditState);
        if (StringUtils.hasText(remark)) {
            detail.setRemark(remark);
        }
        payFeeDetailMapper.updateById(detail);
    }

    public PageResult<PayFeeDetail> queryFeeDetail(String feeId, String communityId, String auditState, Integer page, Integer row) {
        LambdaQueryWrapper<PayFeeDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(feeId), PayFeeDetail::getFeeId, feeId)
                .eq(StringUtils.hasText(communityId), PayFeeDetail::getCommunityId, communityId)
                .eq(StringUtils.hasText(auditState), PayFeeDetail::getAuditState, auditState)
                .orderByDesc(PayFeeDetail::getCreateTime);
        int p = QueryHelper.page(page);
        int r = QueryHelper.row(row);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PayFeeDetail> mp =
                payFeeDetailMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(p, r), wrapper);
        return PageResult.of(mp.getRecords(), mp.getTotal(), p, r);
    }

    public Map<String, Object> feeSummary(String communityId) {
        LambdaQueryWrapper<PayFee> feeWrapper = new LambdaQueryWrapper<>();
        feeWrapper.eq(StringUtils.hasText(communityId), PayFee::getCommunityId, communityId);
        List<PayFee> fees = list(feeWrapper);
        BigDecimal receivable = BigDecimal.ZERO;
        BigDecimal arrears = BigDecimal.ZERO;
        long openCount = 0;
        Map<String, Map<String, Object>> byName = new HashMap<>();
        for (PayFee fee : fees) {
            BigDecimal amount = fee.getAmount() == null ? BigDecimal.ZERO : fee.getAmount();
            receivable = receivable.add(amount);
            boolean open = "2008001".equals(fee.getState());
            if (open) {
                arrears = arrears.add(amount);
                openCount++;
            }
            String name = StringUtils.hasText(fee.getFeeName()) ? fee.getFeeName() : "未命名";
            Map<String, Object> row = byName.computeIfAbsent(name, k -> {
                Map<String, Object> item = new HashMap<>();
                item.put("feeName", k);
                item.put("billCount", 0L);
                item.put("receivable", BigDecimal.ZERO);
                item.put("arrears", BigDecimal.ZERO);
                return item;
            });
            row.put("billCount", (Long) row.get("billCount") + 1);
            row.put("receivable", ((BigDecimal) row.get("receivable")).add(amount));
            if (open) {
                row.put("arrears", ((BigDecimal) row.get("arrears")).add(amount));
            }
        }
        LambdaQueryWrapper<PayFeeDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(StringUtils.hasText(communityId), PayFeeDetail::getCommunityId, communityId)
                .eq(PayFeeDetail::getState, "1400")
                .and(w -> w.ne(PayFeeDetail::getAuditState, "1200").or().isNull(PayFeeDetail::getAuditState));
        List<PayFeeDetail> details = payFeeDetailMapper.selectList(detailWrapper);
        BigDecimal received = details.stream()
                .map(d -> d.getReceivedAmount() == null ? BigDecimal.ZERO : d.getReceivedAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> data = new HashMap<>();
        data.put("billCount", fees.size());
        data.put("openCount", openCount);
        data.put("receivable", receivable);
        data.put("arrears", arrears);
        data.put("received", received);
        data.put("items", byName.values().stream().collect(Collectors.toList()));
        return data;
    }

    public void deleteFee(String feeId) {
        QueryHelper.requireHasText(feeId, "费用ID不能为空");
        PayFee fee = getById(feeId);
        QueryHelper.require(fee != null, "费用不存在");
        fee.setState("2009001");
        updateById(fee);
    }

}
