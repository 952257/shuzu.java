package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDao {

    public void addAddress(Address address) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "insert into address (address, address2, district, city_id, postal_code, phone, location) "
                    + "values (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 0))";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, address.getAddress());
            pstmt.setString(2, address.getAddress2());
            pstmt.setString(3, address.getDistrict());
            pstmt.setInt(4, address.getCityId());
            pstmt.setString(5, address.getPostalCode());
            pstmt.setString(6, address.getPhone());
            pstmt.setString(7, toPointWkt(address.getLongitude(), address.getLatitude()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void updateAddress(Address address) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "update address set address=?, address2=?, district=?, city_id=?, postal_code=?, phone=?, "
                    + "location=ST_GeomFromText(?, 0) where address_id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, address.getAddress());
            pstmt.setString(2, address.getAddress2());
            pstmt.setString(3, address.getDistrict());
            pstmt.setInt(4, address.getCityId());
            pstmt.setString(5, address.getPostalCode());
            pstmt.setString(6, address.getPhone());
            pstmt.setString(7, toPointWkt(address.getLongitude(), address.getLatitude()));
            pstmt.setInt(8, address.getAddressId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void deleteAddress(int addressId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from address where address_id=?");
            pstmt.setInt(1, addressId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public Address getAddressById(int addressId) {
        List<Address> list = selectByCondition(addressId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 动态条件查询，关联 city / country 带出名称
     */
    public List<Address> selectByCondition(Integer addressId, String keyword, Integer cityId) {
        List<Address> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select a.address_id, a.address, a.address2, a.district, a.city_id, a.postal_code, a.phone, a.last_update, "
                            + "ST_AsText(a.location) location_text, ST_X(a.location) longitude, ST_Y(a.location) latitude, "
                            + "c.city, co.country "
                            + "from address a "
                            + "left join city c on a.city_id=c.city_id "
                            + "left join country co on c.country_id=co.country_id "
                            + "where 1=1");
            List<Object> params = new ArrayList<>();
            if (addressId != null) {
                sql.append(" and a.address_id=?");
                params.add(addressId);
            }
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" and (a.address like ? or a.district like ?)");
                String like = "%" + keyword + "%";
                params.add(like);
                params.add(like);
            }
            if (cityId != null) {
                sql.append(" and a.city_id=?");
                params.add(cityId);
            }
            sql.append(" order by a.address_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapJoined(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return list;
    }

    private Address mapJoined(ResultSet rs) throws SQLException {
        Address a = new Address(
                rs.getInt("address_id"),
                rs.getString("address"),
                rs.getString("address2"),
                rs.getString("district"),
                rs.getInt("city_id"),
                rs.getString("postal_code"),
                rs.getString("phone"),
                String.valueOf(rs.getTimestamp("last_update")));
        a.setCityName(rs.getString("city"));
        a.setCountryName(rs.getString("country"));
        a.setLocationText(rs.getString("location_text"));
        double lng = rs.getDouble("longitude");
        a.setLongitude(rs.wasNull() ? null : lng);
        double lat = rs.getDouble("latitude");
        a.setLatitude(rs.wasNull() ? null : lat);
        return a;
    }

    /** 经纬度转 WKT；都为空时默认原点 POINT(0 0) */
    private static String toPointWkt(Double longitude, Double latitude) {
        double x = longitude == null ? 0 : longitude;
        double y = latitude == null ? 0 : latitude;
        return "POINT(" + x + " " + y + ")";
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
