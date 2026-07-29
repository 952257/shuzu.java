package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDao {

    public void add(Store store) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
            PreparedStatement pstmt = conn.prepareStatement("insert into store (manager_staff_id, address_id) values (?, ?)");
            pstmt.setInt(1, store.getManagerStaffId());
            pstmt.setInt(2, store.getAddressId());
            pstmt.executeUpdate();
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void update(Store store) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update store set manager_staff_id=?, address_id=? where store_id=?");
            pstmt.setInt(1, store.getManagerStaffId());
            pstmt.setInt(2, store.getAddressId());
            pstmt.setInt(3, store.getStoreId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void delete(int storeId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from store where store_id=?");
            pstmt.setInt(1, storeId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Store getById(int storeId) {
        List<Store> list = selectByCondition(storeId, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Store> selectByCondition(Integer storeId, Integer managerStaffId) {
        List<Store> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select s.store_id, s.manager_staff_id, s.address_id, s.last_update, "
                            + "concat(st.first_name, ' ', st.last_name) manager_name, a.address address_text "
                            + "from store s "
                            + "left join staff st on s.manager_staff_id=st.staff_id "
                            + "left join address a on s.address_id=a.address_id "
                            + "where 1=1");
            List<Object> params = new ArrayList<>();
            if (storeId != null) {
                sql.append(" and s.store_id=?");
                params.add(storeId);
            }
            if (managerStaffId != null) {
                sql.append(" and s.manager_staff_id=?");
                params.add(managerStaffId);
            }
            sql.append(" order by s.store_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Store s = new Store(rs.getInt("store_id"), rs.getInt("manager_staff_id"),
                        rs.getInt("address_id"), String.valueOf(rs.getTimestamp("last_update")));
                s.setManagerName(rs.getString("manager_name"));
                s.setAddressText(rs.getString("address_text"));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
        return list;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
