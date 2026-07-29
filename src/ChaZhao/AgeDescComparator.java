package ChaZhao;

import java.util.Comparator;

public class AgeDescComparator implements Comparator<User> {

    @Override
    public int compare(User u1, User u2) {
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("排序时用户对象不能为null");
        }
        return u2.getAge() - u1.getAge();
    }
}
