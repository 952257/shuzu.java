package jdbc;

import java.util.Date;
import java.util.List;

/**
 * JDBC
 * Java Database Connectivity
 *
 */
public class TestJDBC {
    public static void main(String[] args) {

//        String url = "jdbc:mysql://localhost:3306/test" +
//                "?userSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT%2B8";
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
//            String insert = "insert into userinfo values(null, ?,?,?,?)";
//            PreparedStatement pstmt = conn.prepareStatement(insert);
//            pstmt.setString(1, "张三");
//            pstmt.setString(2, "男");
//            pstmt.setDate(3,
//                    java.sql.Date.valueOf("2005-01-01"));
//            pstmt.setDouble(4, 180.5);
//            pstmt.executeUpdate();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

        UserDao userDao = new UserDao();
        User user = new User(null, "aaa", "女", new Date(), 170.5);
//        userDao.addUser(user);
        User newUser = new User(2, "bbb", "男", new Date(), 180.5);
//        userDao.updateUser(newUser);

        List<User> users = userDao.queryAll();
        System.out.println(users);
    }


    }
