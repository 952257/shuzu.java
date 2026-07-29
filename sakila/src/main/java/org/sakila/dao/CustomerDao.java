package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    /** 新增客户，create_date 由数据库 NOW() 自动生成 */
    public void add(Customer customer) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into customer (store_id, first_name, last_name, email, address_id, active, create_date) values (?,?,?,?,?,?,NOW())");
            pstmt.setInt(1, customer.getStoreId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getEmail());
            pstmt.setInt(5, customer.getAddressId());
            pstmt.setBoolean(6, customer.isActive());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    /** 按主键更新客户信息 */
    public void update(Customer customer) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "update customer set store_id=?, first_name=?, last_name=?, email=?, address_id=?, active=? where customer_id=?");
            pstmt.setInt(1, customer.getStoreId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getEmail());



            pstmt.setInt(5, customer.getAddressId());
            pstmt.setBoolean(6, customer.isActive());
            pstmt.setInt(7, customer.getCustomerId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    /** 按主键删除客户 */
    public void delete(int customerId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from customer where customer_id=?");
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Customer getById(int customerId) {
        List<Customer> list = selectByCondition(customerId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    /** 动态条件查询：所有参数为 null 时查全部；非 null 时追加 where 条件 */
    public List<Customer> selectByCondition(Integer customerId, String nameKey, Integer storeId) {
        List<Customer> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select c.*, a.address address_text, city.city city_name from customer c "
                            + "left join address a on c.address_id=a.address_id "
                            + "left join city on a.city_id=city.city_id where 1=1");
            List<Object> params = new ArrayList<>();
            if (customerId != null) {
                sql.append(" and c.customer_id=?");
                params.add(customerId);
            }
            if (nameKey != null && !nameKey.isEmpty()) {
                sql.append(" and (c.first_name like ? or c.last_name like ? or c.email like ?)");
                String like = "%" + nameKey + "%";
                params.add(like); params.add(like); params.add(like);
            }
            if (storeId != null) {
                sql.append(" and c.store_id=?");
                params.add(storeId);
            }
            sql.append(" order by c.customer_id");
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

    /** 将一行 ResultSet 映射为 Customer 实体 */
    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setStoreId(rs.getInt("store_id"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setEmail(rs.getString("email"));
        c.setAddressId(rs.getInt("address_id"));
        c.setActive(rs.getBoolean("active"));
        c.setCreateDate(String.valueOf(rs.getTimestamp("create_date")));
        Timestamp lu = rs.getTimestamp("last_update");
        c.setLastUpdate(lu == null ? null : String.valueOf(lu));
        try {
            c.setAddressText(rs.getString("address_text"));
            c.setCityName(rs.getString("city_name"));
        } catch (SQLException ignored) {
        }
        return c;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
