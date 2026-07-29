package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public void add(Category category) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into category (name) values (?)");
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void update(Category category) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update category set name=? where category_id=?");
            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getCategoryId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void delete(int id) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from category where category_id=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Category getById(int id) {
        List<Category> list = selectByCondition(id, null);
        return list.isEmpty() ? null : list.get(0);
    }

    /** 动态条件：编号、名称关键字，回车忽略对应条件 */
    public List<Category> selectByCondition(Integer categoryId, String nameKey) {
        List<Category> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder("select * from category where 1=1");
            List<Object> params = new ArrayList<>();
            if (categoryId != null) {
                sql.append(" and category_id=?");
                params.add(categoryId);
            }
            if (nameKey != null && !nameKey.isEmpty()) {
                sql.append(" and name like ?");
                params.add("%" + nameKey + "%");
            }
            sql.append(" order by category_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("category_id"), rs.getString("name"),
                        String.valueOf(rs.getTimestamp("last_update"))));
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
        return list;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
