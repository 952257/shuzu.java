package com.springboot.others.excel;

import java.util.List;

public interface MemberService {

    /**
     * 获取所有的成员信息
     * @return 成员信息列表
     */
    List<Member> getAllMember();

}