package com.springboot.mybatis.service;

import com.springboot.mybatis.entity.UserMyBatis;

public interface UserService {

    void addTwo(UserMyBatis userMyBatis1, UserMyBatis userMyBatis2);
    void addTwo2(UserMyBatis userMyBatis1, UserMyBatis userMyBatis2);
    void addOne(UserMyBatis userMyBatis);
}
