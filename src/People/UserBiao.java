package People;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UserBiao {
    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        list.add(new User(80001, "aaa", 0, 25));
        list.add(new User(80002, "bbb", 1, 20));
        list.add(new User(80003, "ccc", 0, 19));
        list.add(new User(80004, "ddd", 1, 22));
        list.add(new User(80005, "eee", 0, 21));

        // 找到ID为80005的用户所在的位置，如果不存在，则打印-1
        int a = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == 80005) {
                a = i;
                break;

            }
        }
        System.out.println("ID为80005的用户位置：" + a);

        // 找到年龄最大的用户并将其ID改为80008
        User maxAgeUser = list.get(0);
        for (User user : list) {
            if (user.getAge() > maxAgeUser.getAge()) {
                maxAgeUser = user;
            }
        }
        maxAgeUser.setId(80008);
        System.out.println("年龄最大的用户ID已改为80008：" + maxAgeUser);

        // 找到比所有男性平均年龄小的男性用户信息并打印
        int maleSum = 0;
        int maleCount = 0;
        for (User user : list) {
            if (user.getSex() == 1) {
                maleSum += user.getAge();
                maleCount++;
            }
        }
        double maleAvgAge = (double) maleSum / maleCount;
        System.out.println("男性平均年龄：" + maleAvgAge);
        System.out.println("比男性平均年龄小的男性用户：");
        for (User user : list) {
            if (user.getSex() == 1 && user.getAge() < maleAvgAge) {
                System.out.println(user);
            }
        }

        // 按照年龄从大到小排序
        list.sort(Comparator.comparingInt(User::getAge).reversed());
        System.out.println("按年龄从大到小排序后：");
        for (User user : list) {
            System.out.println(user);
        }

        // 将所有用户放到一个新List里，要求新List中女性在左侧，男性在右侧
        List<User> newList = new ArrayList<>();
        for (User user : list) {
            if (user.getSex() == 0) {
                newList.add(user);
            }
        }
        for (User user : list) {
            if (user.getSex() == 1) {
                newList.add(user);
            }
        }
        System.out.println("新List（女性在左，男性在右）：");
        for (User user : newList) {
            System.out.println(user);
        }

        // 将新List中小于20岁的男性删除
        Iterator<User> iterator = newList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getSex() == 1 && user.getAge() < 20) {
                iterator.remove();
            }
        }
        System.out.println("删除小于20岁的男性后：");
        for (User user : newList) {
            System.out.println(user);
        }
    }
}
