package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：影片数据访问
 */
public class FilmDao {

    public void addFilm(Film film) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "insert into film (title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, film.getTitle());
            pstmt.setString(2, film.getDescription());
            if (film.getReleaseYear() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, film.getReleaseYear());
            }
            pstmt.setInt(4, film.getLanguageId());
            pstmt.setInt(5, film.getRentalDuration());
            pstmt.setBigDecimal(6, film.getRentalRate());
            if (film.getLength() == null) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, film.getLength());
            }
            pstmt.setBigDecimal(8, film.getReplacementCost());
            pstmt.setString(9, film.getRating());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void updateFilm(Film film) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "update film set title=?, description=?, release_year=?, language_id=?, " +
                    "rental_duration=?, rental_rate=?, length=?, replacement_cost=?, rating=? where film_id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, film.getTitle());
            pstmt.setString(2, film.getDescription());
            if (film.getReleaseYear() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, film.getReleaseYear());
            }
            pstmt.setInt(4, film.getLanguageId());
            pstmt.setInt(5, film.getRentalDuration());
            pstmt.setBigDecimal(6, film.getRentalRate());
            if (film.getLength() == null) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, film.getLength());
            }
            pstmt.setBigDecimal(8, film.getReplacementCost());
            pstmt.setString(9, film.getRating());
            pstmt.setInt(10, film.getFilmId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void deleteFilm(int filmId) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "delete from film where film_id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, filmId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public Film getFilmById(int filmId) {
        List<Film> list = selectByCondition(filmId, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Film> selectByCondition(Integer filmId, String titleKey, Integer languageId) {
        List<Film> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder(
                    "select f.*, l.name language_name from film f "
                            + "left join language l on f.language_id=l.language_id where 1=1");
            List<Object> params = new ArrayList<>();
            if (filmId != null) {
                sql.append(" and f.film_id=?");
                params.add(filmId);
            }
            if (titleKey != null && !titleKey.isEmpty()) {
                sql.append(" and f.title like ?");
                params.add("%" + titleKey + "%");
            }
            if (languageId != null) {
                sql.append(" and f.language_id=?");
                params.add(languageId);
            }
            sql.append(" order by f.film_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapFilm(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return list;
    }

    public void listLanguages() {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            String sql = "select language_id, name from language order by language_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("语言编号=" + rs.getInt("language_id")
                        + " | 语言名称=" + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    private Film mapFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        int year = rs.getInt("release_year");
        film.setReleaseYear(rs.wasNull() ? null : year);
        film.setLanguageId(rs.getInt("language_id"));
        int original = rs.getInt("original_language_id");
        film.setOriginalLanguageId(rs.wasNull() ? null : original);
        film.setRentalDuration(rs.getInt("rental_duration"));
        film.setRentalRate(rs.getBigDecimal("rental_rate"));
        int length = rs.getInt("length");
        film.setLength(rs.wasNull() ? null : length);
        film.setReplacementCost(rs.getBigDecimal("replacement_cost"));
        film.setRating(rs.getString("rating"));
        film.setSpecialFeatures(rs.getString("special_features"));
        film.setLastUpdate(String.valueOf(rs.getTimestamp("last_update")));
        try {
            film.setLanguageName(rs.getString("language_name"));
        } catch (SQLException ignored) {
        }
        return film;
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
