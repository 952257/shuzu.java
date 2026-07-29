package org.sakila.dao;

import org.sakila.common.DBInfo;
import org.sakila.entity.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LanguageDao {

    public void add(Language language) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into language (name) values (?)");
            pstmt.setString(1, language.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void update(Language language) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update language set name=? where language_id=?");
            pstmt.setString(1, language.getName());
            pstmt.setInt(2, language.getLanguageId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public void delete(int id) {
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from language where language_id=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
    }

    public Language getById(int id) {
        List<Language> list = selectByCondition(id, null);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Language> selectByCondition(Integer languageId, String nameKey) {
        List<Language> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBInfo.getConnection();
            StringBuilder sql = new StringBuilder("select * from language where 1=1");
            List<Object> params = new ArrayList<>();
            if (languageId != null) {
                sql.append(" and language_id=?");
                params.add(languageId);
            }
            if (nameKey != null && !nameKey.isEmpty()) {
                sql.append(" and name like ?");
                params.add("%" + nameKey + "%");
            }
            sql.append(" order by language_id");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Language(rs.getInt("language_id"), rs.getString("name"),
                        String.valueOf(rs.getTimestamp("last_update"))));
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { close(conn); }
        return list;
    }

    private void close(Connection conn) {
        if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
    }
}
