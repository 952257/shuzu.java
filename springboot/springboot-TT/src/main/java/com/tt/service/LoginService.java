package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.*;
import com.tt.dto.LoginDto;
import com.tt.dto.LoginVo;
import com.tt.mapper.StoreMapper;
import com.tt.mapper.StoreUserMapper;
import com.tt.mapper.UserLoginMapper;
import com.tt.mapper.UserMapper;
import com.tt.po.Store;
import com.tt.po.StoreUser;
import com.tt.po.User;
import com.tt.po.UserLogin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class LoginService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private StoreUserMapper storeUserMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private UserLoginMapper userLoginMapper;
    @Resource
    private JwtUtil jwtUtil;

    public LoginVo login(LoginDto dto) {
        QueryHelper.requireHasText(dto.getUsername(), "用户名不能为空");
        QueryHelper.requireHasText(dto.getPasswd(), "密码不能为空");

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getTel, dto.getUsername()));
        }
        if (user == null) {
            throw new ServiceException(ServiceExceptionEnum.UNAUTHORIZED);
        }

        if (!PasswordUtil.matches(dto.getPasswd(), user.getPassword())) {
            throw new ServiceException(ServiceExceptionEnum.UNAUTHORIZED);
        }
        if (!"ADMIN".equals(user.getRole()) && !"STAFF".equals(user.getRole())) {
            throw new ServiceException(ServiceExceptionEnum.ROLE_NOT_ALLOW);
        }

        StoreUser storeUser = storeUserMapper.selectOne(new LambdaQueryWrapper<StoreUser>().eq(StoreUser::getUserId, user.getUserId()));
        String storeId = storeUser == null ? null : storeUser.getStoreId();
        if (storeUser != null) {
            Store store = storeMapper.selectById(storeUser.getStoreId());
            if (store != null && "48002".equals(store.getState())) {
                throw new ServiceException(ServiceExceptionEnum.STORE_DISABLED);
            }
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getName(), user.getRole(), storeId);
        UserLogin log = new UserLogin();
        log.setLoginId(IdGenerator.nextId());
        log.setUserId(user.getUserId());
        log.setUserName(user.getName());
        log.setLoginTime(new Date());
        log.setSource("WEB");
        userLoginMapper.insert(log);

        LoginVo vo = new LoginVo();
        vo.setUserId(user.getUserId());
        vo.setUserName(user.getName());
        vo.setToken(token);
        vo.setRole(user.getRole());
        return vo;
    }
}
