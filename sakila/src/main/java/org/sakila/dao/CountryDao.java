package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {

    public void addCountry(Country country) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into country (country) values (?)");
            pstmt.setString(1, country.getCountry());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void updateCountry(Country country) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update country set country=? where country_id=?");
            pstmt.setString(1, country.getCountry());
            pstmt.setInt(2, country.getCountryId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void deleteCountry(int countryId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from country where country_id=?");
            pstmt.setInt(1, countryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Country getById(int id) {
        List<Country> list = selectByCondition(id, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Country> selectByCondition(Integer countryId, String nameKey) {
        List<Country> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder("select * from country where 1=1");
            List<Object> params = new ArrayList<>();
            if (countryId != null) {
                sql.append(" and country_id=?");
                params.add(countryId);
            }
            if (nameKey != null && !nameKey.isEmpty()) {
                sql.append(" and country like ?");
                params.add("%" + nameKey + "%");
            }
            sql.append(" order by country_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Country(rs.getInt("country_id"), rs.getString("country"),
                        String.valueOf(rs.getTimestamp("last_update"))));
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
        return list;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
