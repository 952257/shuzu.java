package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：城市（可按城市名模糊、国家编号条件查询，并关联国家名称）
 */
public class CityDao {

    public void addCity(City city) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into city (city, country_id) values (?, ?)");
            pstmt.setString(1, city.getCity());
            pstmt.setInt(2, city.getCountryId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void updateCity(City city) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "update city set city=?, country_id=? where city_id=?");
            pstmt.setString(1, city.getCity());
            pstmt.setInt(2, city.getCountryId());
            pstmt.setInt(3, city.getCityId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void deleteCity(int cityId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "delete from city where city_id=?");
            pstmt.setInt(1, cityId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public City getById(int cityId) {
        List<City> list = selectByCondition(cityId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 动态条件查询（仿教学项目）：
     * cityId / cityKey / countryId 为空则忽略对应条件。
     * 关联 country 表带出国家名称。
     */
    public List<City> selectByCondition(Integer cityId, String cityKey, Integer countryId) {
        List<City> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select city.city_id, city.city, city.country_id, city.last_update, country.country "
                            + "from city left join country using (country_id) where 1=1");
            List<Object> params = new ArrayList<>();
            if (cityId != null) {
                sql.append(" and city.city_id=?");
                params.add(cityId);
            }
            if (cityKey != null && !cityKey.isEmpty()) {
                sql.append(" and city.city like ?");
                params.add("%" + cityKey + "%");
            }
            if (countryId != null && countryId != 0) {
                sql.append(" and city.country_id = ?");
                params.add(countryId);
            }
            sql.append(" order by city.city_id");
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

    private City mapJoined(ResultSet rs) throws SQLException {
        City city = new City(
                rs.getInt("city_id"),
                rs.getString("city"),
                rs.getInt("country_id"),
                String.valueOf(rs.getTimestamp("last_update")));
        city.setCountryName(rs.getString("country"));
        return city;
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
