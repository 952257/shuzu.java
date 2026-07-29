package ChaZhao;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        try {
            // 2. 创建User对象并放入ArrayList
            ArrayList<User> list = createUserList();

            // 3. 找到ID为80005的用户所在位置
            int index = userService.findIndexById(list, 80005);
            System.out.println("ID为80005的用户位置：" + index);

            // 4. 找到年龄最大的用户并将其ID改为80008
            userService.updateMaxAgeUserId(list, 80008);

            // 5. 找到比所有男性平均年龄小的男性用户并打印
            userService.printMalesYoungerThanAvg(list);

            // 6. 按照年龄从大到小排序
            userService.sortByAgeDesc(list);
            userService.printList(list, "按年龄从大到小排序后：");

            // 7. 新List中女性在左侧，男性在右侧
            ArrayList<User> newList = userService.splitBySex(list);
            userService.printList(newList, "女性在前、男性在后的新List：");

            // 8. 新建User对象并插入到list头部
            User newUser = new User(90001, "www", 0, 25);
            userService.insertAtHead(list, newUser);
            userService.printList(list, "插入新用户后的list：");

        } catch (UserException e) {
            System.err.println("用户业务异常：" + e.getMessage());
        } catch (Exception e) {
            System.err.println("系统异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ArrayList<User> createUserList() throws UserException {
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(80001, "aaa", 0, 25));
        list.add(new User(80002, "bbb", 1, 20));
        list.add(new User(80003, "ccc", 0, 19));
        list.add(new User(80004, "ddd", 1, 22));
        list.add(new User(80005, "eee", 0, 21));
        return list;
    }
}
