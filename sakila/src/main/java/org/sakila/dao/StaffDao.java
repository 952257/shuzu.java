package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDao {

    /** 新增员工（临时关闭外键检查以应对 store↔staff 循环依赖） */
    public void add(Staff staff) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into staff (first_name, last_name, address_id, email, store_id, active, username, password) values (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setInt(3, staff.getAddressId());
            pstmt.setString(4, staff.getEmail());
            pstmt.setInt(5, staff.getStoreId());
            pstmt.setBoolean(6, staff.isActive());
            pstmt.setString(7, staff.getUsername());
            pstmt.setString(8, staff.getPassword());
            pstmt.executeUpdate();
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    /** 按主键更新员工信息 */
    public void update(Staff staff) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "update staff set first_name=?, last_name=?, address_id=?, email=?, store_id=?, active=?, username=?, password=? where staff_id=?");
            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setInt(3, staff.getAddressId());
            pstmt.setString(4, staff.getEmail());
            pstmt.setInt(5, staff.getStoreId());
            pstmt.setBoolean(6, staff.isActive());
            pstmt.setString(7, staff.getUsername());
            pstmt.setString(8, staff.getPassword());
            pstmt.setInt(9, staff.getStaffId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    /** 按主键删除员工 */
    public void delete(int staffId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from staff where staff_id=?");
            pstmt.setInt(1, staffId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Staff getById(int staffId) {
        List<Staff> list = selectByCondition(staffId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    /** 动态条件查询：支持按员工ID、姓名/用户名关键字、商店ID过滤 */
    public List<Staff> selectByCondition(Integer staffId, String nameKey, Integer storeId) {
        List<Staff> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select s.*, a.address address_text, c.city city_name from staff s "
                            + "left join address a on s.address_id=a.address_id "
                            + "left join city c on a.city_id=c.city_id where 1=1");
            List<Object> params = new ArrayList<>();
            if (staffId != null) {
                sql.append(" and s.staff_id=?");
                params.add(staffId);
            }
            if (nameKey != null && !nameKey.isEmpty()) {
                sql.append(" and (s.first_name like ? or s.last_name like ? or s.username like ?)");
                String like = "%" + nameKey + "%";
                params.add(like); params.add(like); params.add(like);
            }
            if (storeId != null) {
                sql.append(" and s.store_id=?");
                params.add(storeId);
            }
            sql.append(" order by s.staff_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
        return list;
    }

    /** 将一行 ResultSet 映射为 Staff 实体 */
    private Staff map(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setStaffId(rs.getInt("staff_id"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setAddressId(rs.getInt("address_id"));
        s.setEmail(rs.getString("email"));
        s.setStoreId(rs.getInt("store_id"));
        s.setActive(rs.getBoolean("active"));
        s.setUsername(rs.getString("username"));
        s.setPassword(rs.getString("password"));
        s.setLastUpdate(String.valueOf(rs.getTimestamp("last_update")));
        try {
            s.setAddressText(rs.getString("address_text"));
            s.setCityName(rs.getString("city_name"));
        } catch (SQLException ignored) {
        }
        return s;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
