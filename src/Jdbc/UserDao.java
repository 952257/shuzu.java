package Jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：Data Access Object 数据访问对象
 * persons 表数据访问对象
 */
public class UserDao {

    String url = "jdbc:mysql://127.0.0.1:3306/persons" +
            "?userSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT%2B8";
    String username = "root";
    String password = "hjx127307";

    /**
     * 清空表数据（方便重复运行）
     */
    public void clearPersons() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String delete = "delete from persons";
            PreparedStatement pstmt = conn.prepareStatement(delete);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 插入一条 Person 数据
     */
    public void addPerson(Person person) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String insert = "insert into persons (ID, NAME, HEIGHT, SEX, BIRTHDAY) values (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setInt(1, person.getId());
            pstmt.setString(2, person.getName());
            pstmt.setDouble(3, person.getHeight());
            pstmt.setInt(4, person.getSex());
            pstmt.setDate(5, new java.sql.Date(person.getBirthday().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 修改 Person 数据（关闭自动提交，最后提交事务）
     */
    public void updatePerson(Person person) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false); // 不自动提交
            String update = "update persons set NAME=?, HEIGHT=?, SEX=?, BIRTHDAY=? where ID=?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setString(1, person.getName());
            pstmt.setDouble(2, person.getHeight());
            pstmt.setInt(3, person.getSex());
            pstmt.setDate(4, new java.sql.Date(person.getBirthday().getTime()));
            pstmt.setInt(5, person.getId());
            pstmt.executeUpdate();
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // 回滚
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 查询 sex 指定值的所有数据，封装为 Person 放入 ArrayList
     */
    public List<Person> queryBySex(int sex) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String select = "select * from persons where SEX=?";
            PreparedStatement pstmt = conn.prepareStatement(select);
            pstmt.setInt(1, sex);
            ResultSet rs = pstmt.executeQuery();
            List<Person> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Person(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getDouble("HEIGHT"),
                        rs.getInt("SEX"),
                        rs.getDate("BIRTHDAY")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
