package day13.exercise.usermanage.service.impl;

import day13.exercise.usermanage.entity.UserPo;
import day13.exercise.usermanage.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public void add(List<UserPo> list, UserPo po) {
        list.add(0, po);
    }

    @Override
    public void getUserById(List<UserPo> list, int id) {
        if(!list.isEmpty()){
            Iterator<UserPo> iterator = list.iterator();
            int index = 0;
            boolean isExist = false;
            while (iterator.hasNext()){
                UserPo userPo = iterator.next();
                if(userPo.getId() == id){
                    isExist = true;
                    break;
                }
                index ++;
            }
            if(isExist){
                System.out.println(index);
            }else{
                System.out.println("-1");
            }
        }
    }

    @Override
    public UserPo getMaxAge(List<UserPo> list) {
        UserPo maxUserPo = null;
        if(!list.isEmpty()){
            maxUserPo = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if(list.get(i).getAge() > maxUserPo.getAge()){
                    maxUserPo = list.get(i);
                }
            }
            maxUserPo.setId(80008);
            System.out.println("===============获取到最大年龄用户===============》start");
            System.out.println(maxUserPo);
            System.out.println("===============获取到最大年龄用户===============》end");
        }
        return maxUserPo;
    }

    @Override
    public BigDecimal getManAvgAge(List<UserPo> list) {
        // 1.先过滤出所有男性
        // 2.算出所有男性年龄总和
        // 3.算出所有男性人数
        // 4.算出所有男性平均年龄
        BigDecimal bgSum = new BigDecimal("0");
        int manCount = 0;
        if(!list.isEmpty()){
            for(UserPo userPo : list){
                if(userPo.getSex() == 1){
                    bgSum = bgSum.add(new BigDecimal(userPo.getAge()));
                    manCount ++;
                }
            }

        }
        if(bgSum.intValue() != 0 && manCount != 0){
            return bgSum.divide(new BigDecimal(String.valueOf(manCount)), 1, RoundingMode.HALF_UP);

        }

        return null;
    }

    @Override
    public boolean updateUser(UserPo userPo) {
        return false;
    }

    @Override
    public void method1(List<UserPo> list, BigDecimal avgAge) {
        if(!list.isEmpty() && avgAge != null){
            for(UserPo userPo : list){
                if(userPo.getSex() == 1 && userPo.getAge() < avgAge.doubleValue()){
                    System.out.println(userPo);
                }
            }
        }
    }

    @Override
    public void method2(List<UserPo> list) {
        List<UserPo> newList = new ArrayList<>();
        if(!list.isEmpty()){
            for(UserPo userPo : list){
                if(userPo.getSex() == 0){
                    newList.add(userPo);
                }
            }

            for(UserPo userPo : list){
                if(userPo.getSex() == 1){
                    newList.add(userPo);
                }
            }

        }
        System.out.println("===============重新组装集合：女性在前面，男性在后面===============》start");
        System.out.println(newList);
        System.out.println("===============重新组装集合：女性在前面，男性在后面===============》start");
    }

    public static void main(String[] args) {
        BigDecimal bg1 = new BigDecimal("50");
        System.out.println(bg1.toString());
        bg1 = bg1.add(new BigDecimal("100"));
        System.out.println(bg1.toString()); // 150
    }
}
