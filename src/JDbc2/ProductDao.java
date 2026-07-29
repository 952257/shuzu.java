package JDbc2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：Data Access Object 数据访问对象
 * product 表数据访问对象
 */
public class ProductDao {

    String url = "jdbc:mysql://127.0.0.1:3306/product" +
            "?userSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT%2B8";
    String username = "root";
    String password = "hjx127307";

    /**
     * 新增产品
     */
    public void addProduct(Product product) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String insert = "insert into product (pname, price, stock, category) values (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setString(1, product.getPname());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setString(4, product.getCategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据产品ID删除
     */
    public void delProductById(int pid) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String delete = "delete from product where pid=?";
            PreparedStatement pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, pid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据ID修改产品价格
     */
    public void updatePrice(int pid, double newPrice) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String update = "update product set price=? where pid=?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, pid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据ID查询单个产品
     */
    public Product getProductById(int pid) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String select = "select * from product where pid=?";
            PreparedStatement pstmt = conn.prepareStatement(select);
            pstmt.setInt(1, pid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("pid"),
                        rs.getString("pname"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 查询全部产品
     */
    public List<Product> listAllProduct() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String select = "select * from product";
            PreparedStatement pstmt = conn.prepareStatement(select);
            ResultSet rs = pstmt.executeQuery();
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("pid"),
                        rs.getString("pname"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 根据产品名称模糊查询
     */
    public List<Product> listProductByLikeName(String keyword) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            String select = "select * from product where pname like ?";
            PreparedStatement pstmt = conn.prepareStatement(select);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("pid"),
                        rs.getString("pname"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
