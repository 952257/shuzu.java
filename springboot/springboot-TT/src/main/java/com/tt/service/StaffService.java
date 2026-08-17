package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalDelete;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.PasswordUtil;
import com.tt.common.QueryHelper;
import com.tt.common.ServiceException;
import com.tt.common.ServiceExceptionEnum;
import com.tt.common.UserContext;
import com.tt.mapper.StoreUserMapper;
import com.tt.mapper.UserMapper;
import com.tt.po.StoreUser;
import com.tt.po.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService extends PhysicalServiceImpl<UserMapper, User> {

    @Resource
    private StoreUserMapper storeUserMapper;
    @Resource
    private PhysicalDelete physicalDelete;

    public PageResult<User> listStaff(String name, String tel, Integer page, Integer row) {
        UserContext.requireAdmin();
        List<String> userIds = storeUserIds();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (userIds.isEmpty()) {
            wrapper.eq(User::getUserId, "-1");
        } else {
            wrapper.in(User::getUserId, userIds);
        }
        wrapper.like(StringUtils.hasText(name), User::getName, name)
                .like(StringUtils.hasText(tel), User::getTel, tel)
                .orderByDesc(User::getCreateTime);
        PageResult<User> result = QueryHelper.toPage(this, wrapper, page, row);
        result.getData().forEach(u -> u.setPassword(null));
        return result;
    }

    @Transactional
    public String addStaff(User user) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(user.getName(), "员工姓名不能为空");
        QueryHelper.requireHasText(user.getUsername(), "用户名不能为空");
        QueryHelper.requireHasText(user.getPassword(), "密码不能为空");
        QueryHelper.require(count(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())) == 0, "用户名已存在");
        user.setUserId(IdGenerator.nextId());
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        if (StringUtils.hasText(user.getRole())) {
            QueryHelper.require("ADMIN".equals(user.getRole()) || "STAFF".equals(user.getRole()), "角色不合法");
        } else {
            user.setRole("STAFF");
        }
        save(user);
        StoreUser rel = new StoreUser();
        rel.setStoreUserId(IdGenerator.nextId());
        QueryHelper.requireHasText(UserContext.getStoreId(), "请重新登录后再操作");
        rel.setStoreId(UserContext.getStoreId());
        rel.setUserId(user.getUserId());
        rel.setRelCd("600311000002");
        storeUserMapper.insert(rel);
        return user.getUserId();
    }

    public void modifyStaff(User user) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(user.getUserId(), "员工ID不能为空");
        requireSameStore(user.getUserId());
        if (StringUtils.hasText(user.getUsername())) {
            QueryHelper.require(count(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, user.getUsername())
                    .ne(User::getUserId, user.getUserId())) == 0, "用户名已存在");
        }
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        if (StringUtils.hasText(user.getRole())) {
            QueryHelper.require("ADMIN".equals(user.getRole()) || "STAFF".equals(user.getRole()), "角色不合法");
        }
        updateById(user);
    }

    @Transactional
    public void deleteStaff(String userId) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(userId, "员工ID不能为空");
        QueryHelper.require(!userId.equals(UserContext.getUserId()), "不能删除当前登录账号");
        requireSameStore(userId);
        physicalDelete.byColumn(StoreUser.class, "user_id", userId);
        removeById(userId);
    }

    private void requireSameStore(String userId) {
        QueryHelper.requireHasText(UserContext.getStoreId(), "请重新登录后再操作");
        StoreUser rel = storeUserMapper.selectOne(new LambdaQueryWrapper<StoreUser>()
                .eq(StoreUser::getUserId, userId)
                .last("limit 1"));
        if (rel == null || !UserContext.getStoreId().equals(rel.getStoreId())) {
            throw new ServiceException(ServiceExceptionEnum.FORBIDDEN);
        }
    }

    private List<String> storeUserIds() {
        QueryHelper.requireHasText(UserContext.getStoreId(), "请重新登录后再操作");
        return storeUserMapper.selectList(new LambdaQueryWrapper<StoreUser>()
                        .eq(StoreUser::getStoreId, UserContext.getStoreId()))
                .stream()
                .map(StoreUser::getUserId)
                .collect(Collectors.toList());
    }
}
