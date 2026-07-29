package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDao {

    public void add(Rental rental) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into rental (rental_date, inventory_id, customer_id, return_date, staff_id) values (?,?,?,?,?)");
            pstmt.setTimestamp(1, Timestamp.valueOf(rental.getRentalDate()));
            pstmt.setInt(2, rental.getInventoryId());
            pstmt.setInt(3, rental.getCustomerId());
            if (rental.getReturnDate() == null || rental.getReturnDate().isEmpty()) {
                pstmt.setNull(4, Types.TIMESTAMP);
            } else {
                pstmt.setTimestamp(4, Timestamp.valueOf(rental.getReturnDate()));
            }
            pstmt.setInt(5, rental.getStaffId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void update(Rental rental) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "update rental set rental_date=?, inventory_id=?, customer_id=?, return_date=?, staff_id=? where rental_id=?");
            pstmt.setTimestamp(1, Timestamp.valueOf(rental.getRentalDate()));
            pstmt.setInt(2, rental.getInventoryId());
            pstmt.setInt(3, rental.getCustomerId());
            if (rental.getReturnDate() == null || rental.getReturnDate().isEmpty()) {
                pstmt.setNull(4, Types.TIMESTAMP);
            } else {
                pstmt.setTimestamp(4, Timestamp.valueOf(rental.getReturnDate()));
            }
            pstmt.setInt(5, rental.getStaffId());
            pstmt.setInt(6, rental.getRentalId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void delete(int rentalId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from rental where rental_id=?");
            pstmt.setInt(1, rentalId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Rental getById(int rentalId) {
        List<Rental> list = selectByCondition(rentalId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Rental> selectByCondition(Integer rentalId, Integer customerId, Integer staffId) {
        List<Rental> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select r.*, "
                            + "concat(c.first_name, ' ', c.last_name) customer_name, "
                            + "concat(s.first_name, ' ', s.last_name) staff_name, "
                            + "f.film_id, f.title film_title "
                            + "from rental r "
                            + "left join customer c on r.customer_id=c.customer_id "
                            + "left join staff s on r.staff_id=s.staff_id "
                            + "left join inventory i on r.inventory_id=i.inventory_id "
                            + "left join film f on i.film_id=f.film_id where 1=1");
            List<Object> params = new ArrayList<>();
            if (rentalId != null) {
                sql.append(" and r.rental_id=?");
                params.add(rentalId);
            }
            if (customerId != null) {
                sql.append(" and r.customer_id=?");
                params.add(customerId);
            }
            if (staffId != null) {
                sql.append(" and r.staff_id=?");
                params.add(staffId);
            }
            sql.append(" order by r.rental_id");
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

    private Rental map(ResultSet rs) throws SQLException {
        Rental r = new Rental();
        r.setRentalId(rs.getInt("rental_id"));
        r.setRentalDate(String.valueOf(rs.getTimestamp("rental_date")));
        r.setInventoryId(rs.getInt("inventory_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        Timestamp ret = rs.getTimestamp("return_date");
        r.setReturnDate(ret == null ? null : String.valueOf(ret));
        r.setStaffId(rs.getInt("staff_id"));
        r.setLastUpdate(String.valueOf(rs.getTimestamp("last_update")));
        try {
            r.setCustomerName(rs.getString("customer_name"));
            r.setStaffName(rs.getString("staff_name"));
            r.setFilmTitle(rs.getString("film_title"));
            int fid = rs.getInt("film_id");
            r.setFilmId(rs.wasNull() ? null : fid);
        } catch (SQLException ignored) {
        }
        return r;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
