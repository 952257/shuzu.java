package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

    public void add(Payment payment) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into payment (customer_id, staff_id, rental_id, amount, payment_date) values (?,?,?,?,?)");
            pstmt.setInt(1, payment.getCustomerId());
            pstmt.setInt(2, payment.getStaffId());
            if (payment.getRentalId() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, payment.getRentalId());
            }
            pstmt.setBigDecimal(4, payment.getAmount());
            pstmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void update(Payment payment) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "update payment set customer_id=?, staff_id=?, rental_id=?, amount=?, payment_date=? where payment_id=?");
            pstmt.setInt(1, payment.getCustomerId());
            pstmt.setInt(2, payment.getStaffId());
            if (payment.getRentalId() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, payment.getRentalId());
            }
            pstmt.setBigDecimal(4, payment.getAmount());
            pstmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            pstmt.setInt(6, payment.getPaymentId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void delete(int paymentId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from payment where payment_id=?");
            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Payment getById(int paymentId) {
        List<Payment> list = selectByCondition(paymentId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Payment> selectByCondition(Integer paymentId, Integer customerId, Integer staffId) {
        List<Payment> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select p.*, "
                            + "concat(c.first_name, ' ', c.last_name) customer_name, "
                            + "concat(s.first_name, ' ', s.last_name) staff_name "
                            + "from payment p "
                            + "left join customer c on p.customer_id=c.customer_id "
                            + "left join staff s on p.staff_id=s.staff_id where 1=1");
            List<Object> params = new ArrayList<>();
            if (paymentId != null) {
                sql.append(" and p.payment_id=?");
                params.add(paymentId);
            }
            if (customerId != null) {
                sql.append(" and p.customer_id=?");
                params.add(customerId);
            }
            if (staffId != null) {
                sql.append(" and p.staff_id=?");
                params.add(staffId);
            }
            sql.append(" order by p.payment_id");
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

    private Payment map(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setCustomerId(rs.getInt("customer_id"));
        p.setStaffId(rs.getInt("staff_id"));
        int rentalId = rs.getInt("rental_id");
        p.setRentalId(rs.wasNull() ? null : rentalId);
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentDate(String.valueOf(rs.getTimestamp("payment_date")));
        Timestamp lu = rs.getTimestamp("last_update");
        p.setLastUpdate(lu == null ? null : String.valueOf(lu));
        try {
            p.setCustomerName(rs.getString("customer_name"));
            p.setStaffName(rs.getString("staff_name"));
        } catch (SQLException ignored) {
        }
        return p;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
