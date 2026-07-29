package Jdbc;

import java.sql.Date;
import java.util.List;

/**
 * JDBC
 * Java Database Connectivity
 */
public class TestJdbc {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        userDao.clearPersons();

        // 任务2：插入数据
        Person p1 = new Person(1, "Sam", 170.5, 1, Date.valueOf("1996-12-12"));
        Person p2 = new Person(2, "张曼玉", 168.5, 0, Date.valueOf("1976-08-08"));
        Person p3 = new Person(3, "特朗普", 180.0, 1, Date.valueOf("1950-01-01"));
        userDao.addPerson(p1);
        userDao.addPerson(p2);
        userDao.addPerson(p3);
        System.out.println("插入数据成功");

        // 任务3、4：关闭自动提交，修改特朗普身高和生日，提交事务
        Person trump = new Person(3, "特朗普", 185.5, 1, Date.valueOf("1955-11-11"));
        userDao.updatePerson(trump);
        System.out.println("修改特朗普信息成功，事务已提交");

        // 任务5、6：查询 sex=1 的数据，构造 Person 对象放入 ArrayList
        List<Person> persons = userDao.queryBySex(1);
        System.out.println(persons);
    }
}
