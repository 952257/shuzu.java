package JDbc2;

/**
 * 实体类
 * 跟表 product 对应
 */
public class Product {

    private Integer pid;
    private String pname;
    private Double price;
    private Integer stock;
    private String category;

    public Product() {
    }

    public Product(Integer pid, String pname, Double price, Integer stock, String category) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", pname='" + pname + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }
}
