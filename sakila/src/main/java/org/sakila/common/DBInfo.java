package org.sakila.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接信息与获取连接
 */
public class DBInfo {

    public static final String URL = "jdbc:mysql://localhost:3306/sakila"
            + "?useSSL=false&allowPublicKeyRetrieval=true"
            + "&useUnicode=true&characterEncoding=UTF-8"
            + "&serverTimezone=GMT%2B8";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "hjx127307";

    static {
        try {
            // 显式加载 MySQL 驱动，避免 “No suitable driver found”
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "未找到 MySQL 驱动，请确认已添加 mysql-connector-j 依赖: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * 启动前探测数据库是否可连接。
     * @return null 表示成功；非 null 为失败原因（可直接打印）
     */
    public static String checkConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null || conn.isClosed()) {
                return "无法获取有效的数据库连接。";
            }
            // 轻量探测，确认连接真正可用
            conn.createStatement().execute("SELECT 1");
            return null;
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
