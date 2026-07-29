package day13.exercise.usermanage.service;

import day13.exercise.usermanage.entity.UserPo;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    void add(List<UserPo> list, UserPo po);
    void getUserById(List<UserPo> list, int id);
    UserPo getMaxAge(List<UserPo> list);
    BigDecimal getManAvgAge(List<UserPo> list);
    boolean updateUser(UserPo userPo);

    // 输出比平均年龄小的用户
    void method1(List<UserPo> list, BigDecimal avgAge);

    // 重新组装集合，女性放在集合前面，男性放在集合后面
    void method2(List<UserPo> list);



}
