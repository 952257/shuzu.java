package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.CommunityGuard;
import com.tt.common.QueryHelper;
import com.tt.mapper.AccountDetailMapper;
import com.tt.mapper.AccountMapper;
import com.tt.po.Account;
import com.tt.po.AccountDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountService extends PhysicalServiceImpl<AccountMapper, Account> {

    @Resource
    private AccountDetailMapper accountDetailMapper;

    public PageResult<Account> queryOwnerAccount(String communityId, String objId, Integer page, Integer row) {
        CommunityGuard.requireCommunity(communityId);
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getCommunityId, communityId)
                .eq(StringUtils.hasText(objId), Account::getObjId, objId)
                .orderByDesc(Account::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    @Transactional
    public String ownerPrestoreAccount(String acctId, BigDecimal amount, String remark) {
        QueryHelper.requireHasText(acctId, "账户ID不能为空");
        QueryHelper.require(amount != null && amount.compareTo(BigDecimal.ZERO) > 0, "预存金额必须大于0");
        Account account = getById(acctId);
        CommunityGuard.mustBelong(account, Account::getCommunityId, "账户不存在");
        QueryHelper.require(account.getAmount() != null, "账户余额异常");
        account.setAmount(account.getAmount().add(amount));
        updateById(account);
        AccountDetail detail = new AccountDetail();
        detail.setDetailId(IdGenerator.nextId());
        detail.setAcctId(acctId);
        detail.setDetailType("1001");
        detail.setAmount(amount);
        detail.setRemark(remark);
        detail.setState("1001");
        accountDetailMapper.insert(detail);
        return detail.getDetailId();
    }

    public PageResult<AccountDetail> listAccountDetail(String acctId, Integer page, Integer row) {
        QueryHelper.requireHasText(acctId, "账户ID不能为空");
        Account account = getById(acctId);
        CommunityGuard.mustBelong(account, Account::getCommunityId, "账户不存在");
        LambdaQueryWrapper<AccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountDetail::getAcctId, acctId)
                .orderByDesc(AccountDetail::getCreateTime);
        int p = QueryHelper.page(page);
        int r = QueryHelper.row(row);
        Page<AccountDetail> mp = accountDetailMapper.selectPage(new Page<>(p, r), wrapper);
        return PageResult.of(mp.getRecords(), mp.getTotal(), p, r);
    }

    @Transactional
    public void cancelAccountDetail(String detailId) {
        QueryHelper.requireHasText(detailId, "明细ID不能为空");
        AccountDetail detail = accountDetailMapper.selectById(detailId);
        QueryHelper.require(detail != null, "明细不存在");
        QueryHelper.require(!"2002".equals(detail.getState()), "该明细已撤销");
        Account account = getById(detail.getAcctId());
        CommunityGuard.mustBelong(account, Account::getCommunityId, "账户不存在");
        if ("1001".equals(detail.getDetailType())) {
            account.setAmount(account.getAmount().subtract(detail.getAmount()));
        } else {
            account.setAmount(account.getAmount().add(detail.getAmount()));
        }
        updateById(account);
        detail.setState("2002");
        detail.setDetailType("3003");
        accountDetailMapper.updateById(detail);
    }
}
