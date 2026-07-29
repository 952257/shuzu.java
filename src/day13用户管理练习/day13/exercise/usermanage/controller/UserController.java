package day13.exercise.usermanage.controller;

import day13.exercise.usermanage.entity.UserPo;
import day13.exercise.usermanage.service.UserService;
import day13.exercise.usermanage.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;

public class UserController {
    public static ArrayList<UserPo> list = new ArrayList<>();
    private static UserService userService;

    static{
        userService = new UserServiceImpl();
        list.add(new UserPo(80001, "武大郎", 40, 1));
        list.add(new UserPo(80002, "潘金莲", 18, 0));
        list.add(new UserPo(80003, "武松", 30, 1));
        list.add(new UserPo(80004, "西门庆", 30, 1));
        list.add(new UserPo(80005, "潘巧云", 18, 0));
        list.add(new UserPo(80006, "贾氏", 18, 0));
        list.add(new UserPo(80007, "阎婆惜", 18, 0));
    }
    public static void main(String[] args) {
        // ctrl + alt + b 查看方法实现

        // 操作该List，找到ID为80005的用户所在的位置，如果不存在，则打印-1
        userService.getUserById(list, 80005);

        // 操作该List，找到年龄最大的用户并将其ID改为80008
        userService.getMaxAge(list);

        //操作该List，找到比所有男性平均年龄小的男性用户信息并打印
        BigDecimal manAvgAge = userService.getManAvgAge(list);
        System.out.println(manAvgAge);
        userService.method1(list, manAvgAge);

        // 操作该List，将所有用户放到一个新List里，要求新List中女性在左侧，男性在右侧
        userService.method2(list);

        //按照下面表格信息新建一个User对象，最后将该对象插入到list的头部
        userService.add(list, new UserPo(80007, "鲁智深", 30, 1));
        System.out.println(list);

    }
}
