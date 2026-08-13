package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.PasswordUtil;
import com.tt.common.QueryHelper;
import com.tt.mapper.StoreUserMapper;
import com.tt.mapper.UserMapper;
import com.tt.po.StoreUser;
import com.tt.po.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class StaffService extends ServiceImpl<UserMapper, User> {

    @Resource
    private StoreUserMapper storeUserMapper;

    public PageResult<User> listStaff(String name, String tel, Integer page, Integer row) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), User::getName, name)
                .like(StringUtils.hasText(tel), User::getTel, tel)
                .orderByDesc(User::getCreateTime);
        PageResult<User> result = QueryHelper.toPage(this, wrapper, page, row);
        result.getData().forEach(u -> u.setPassword(null));
        return result;
    }

    @Transactional
    public String addStaff(User user) {
        QueryHelper.requireHasText(user.getName(), "员工姓名不能为空");
        QueryHelper.requireHasText(user.getUsername(), "用户名不能为空");
        QueryHelper.requireHasText(user.getPassword(), "密码不能为空");
        user.setUserId(IdGenerator.nextId());
        user.setPassword(PasswordUtil.passwdMd5(user.getPassword()));
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole("STAFF");
        }
        save(user);
        StoreUser rel = new StoreUser();
        rel.setStoreUserId(IdGenerator.nextId());
        rel.setStoreId(StringUtils.hasText(user.getStoreId()) ? user.getStoreId() : "10001");
        rel.setUserId(user.getUserId());
        rel.setRelCd("600311000002");
        storeUserMapper.insert(rel);
        return user.getUserId();
    }

    public void modifyStaff(User user) {
        QueryHelper.requireHasText(user.getUserId(), "员工ID不能为空");
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(PasswordUtil.passwdMd5(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        updateById(user);
    }

    public void deleteStaff(String userId) {
        QueryHelper.requireHasText(userId, "员工ID不能为空");
        removeById(userId);
    }
}
