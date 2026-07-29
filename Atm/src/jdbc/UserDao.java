package jdbc;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：Data Access Object 数据访问对象
 * 用户数据访问对象
 */
public class UserDao {

    String url = "jdbc:mysql://localhost:3306/test" +
            "?userSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT%2B8";
    String username = "root";
    String password = "123456";

    public void addUser(User user) {
        Connection conn = null;
        try{
         conn = DriverManager.getConnection(url, username, password);
         String  insert = "insert into userinfo values(null, ?,?,?,?)";
         PreparedStatement pstmt = conn.prepareStatement(insert);
         pstmt.setString(1, user.getName());
         pstmt.setString(2, user.getSex());
         pstmt.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
         pstmt.setDouble(4, user.getHeight());
         pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateUser(User user) {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, username, password);
            String  update = "update userinfo set name=?,sex=?,birthday=?,height=? where id=?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSex());
            pstmt.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            pstmt.setDouble(4, user.getHeight());
            pstmt.setDouble(5, user.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public List<User> queryAll() {
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);//不自动提交
            String  update = "select * from userinfo";
            PreparedStatement pstmt = conn.prepareStatement(update);
            ResultSet rs = pstmt.executeQuery();
            List<User> list = new ArrayList<>();
            while(rs.next()){
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("sex"),
                        rs.getDate("birthday"),
                        rs.getDouble("height")
                ));
            }
            conn.commit();
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            try {
                conn.rollback();//回滚
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }finally {
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
