package ChaZhao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class UserServiceImpl implements UserService {

    private void checkListNotNull(ArrayList<User> list) throws UserException {
        if (list == null) {
            throw new UserException("用户列表不能为null");
        }
    }

    private void checkListNotEmpty(ArrayList<User> list) throws UserException {
        checkListNotNull(list);
        if (list.isEmpty()) {
            throw new UserException("用户列表不能为空");
        }
    }

    @Override
    public int findIndexById(ArrayList<User> list, int id) throws UserException {
        checkListNotNull(list);
        Iterator<User> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user == null) {
                throw new UserException("列表第" + index + "个位置存在空用户对象");
            }
            if (user.getId() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public void updateMaxAgeUserId(ArrayList<User> list, int newId) throws UserException {
        checkListNotEmpty(list);
        if (newId <= 0) {
            throw new UserException("新ID必须大于0，当前值：" + newId);
        }

        User maxAgeUser = null;
        Iterator<User> iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user == null) {
                throw new UserException("列表中存在空用户对象，无法查找年龄最大用户");
            }
            if (maxAgeUser == null || user.getAge() > maxAgeUser.getAge()) {
                maxAgeUser = user;
            }
        }
        maxAgeUser.setId(newId);
        System.out.println("年龄最大的用户ID已改为" + newId + "：" + maxAgeUser);
    }

    @Override
    public void printMalesYoungerThanAvg(ArrayList<User> list) throws UserException {
        checkListNotNull(list);

        int maleCount = 0;
        int maleAgeSum = 0;
        Iterator<User> iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user == null) {
                throw new UserException("列表中存在空用户对象，无法计算男性平均年龄");
            }
            if (user.getSex() == 1) {
                maleCount++;
                maleAgeSum += user.getAge();
            }
        }

        if (maleCount == 0) {
            throw new UserException("列表中没有男性用户，无法计算男性平均年龄");
        }

        double maleAvgAge = (double) maleAgeSum / maleCount;
        System.out.println("男性平均年龄：" + maleAvgAge);
        System.out.println("比男性平均年龄小的男性用户：");

        iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getSex() == 1 && user.getAge() < maleAvgAge) {
                System.out.println(user);
            }
        }
    }

    @Override
    public void sortByAgeDesc(ArrayList<User> list) throws UserException {
        checkListNotNull(list);
        Collections.sort(list, new AgeDescComparator());
    }

    @Override
    public ArrayList<User> splitBySex(ArrayList<User> list) throws UserException {
        checkListNotNull(list);

        ArrayList<User> newList = new ArrayList<>();
        Iterator<User> iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user == null) {
                throw new UserException("列表中存在空用户对象，无法按性别分组");
            }
            if (user.getSex() == 0) {
                newList.add(user);
            }
        }
        iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getSex() == 1) {
                newList.add(user);
            }
        }
        return newList;
    }

    @Override
    public void insertAtHead(ArrayList<User> list, User user) throws UserException {
        checkListNotNull(list);
        if (user == null) {
            throw new UserException("插入的用户对象不能为null");
        }
        list.add(0, user);
    }

    @Override
    public void printList(ArrayList<User> list, String title) throws UserException {
        checkListNotNull(list);
        if (title == null) {
            throw new UserException("打印标题不能为null");
        }

        System.out.println(title);
        if (list.isEmpty()) {
            System.out.println("（列表为空）");
            return;
        }

        Iterator<User> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user == null) {
                throw new UserException("列表第" + index + "个位置存在空用户对象");
            }
            System.out.println(user);
            index++;
        }
    }
}
