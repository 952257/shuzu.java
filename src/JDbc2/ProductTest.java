package JDbc2;

import java.util.List;

/**
 * JDBC
 * Java Database Connectivity
 * 产品信息管理测试类
 */
public class ProductTest {
    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();

        // 1. 查询全部产品
        System.out.println("========== 查询全部产品 ==========");
        List<Product> all = productDao.listAllProduct();
        System.out.println(all);

        // 2. 新增产品
        System.out.println("========== 新增产品 ==========");
        Product product = new Product(null, "蓝牙耳机", 199.00, 100, "数码配件");
        productDao.addProduct(product);
        System.out.println("新增成功");
        System.out.println(productDao.listAllProduct());

        // 3. 根据ID查询单个产品
        System.out.println("========== 根据ID查询 ==========");
        Product p = productDao.getProductById(1);
        System.out.println(p);

        // 4. 根据ID修改价格
        System.out.println("========== 修改价格 ==========");
        productDao.updatePrice(1, 79.90);
        System.out.println(productDao.getProductById(1));

        // 5. 模糊查询
        System.out.println("========== 模糊查询（关键字：键） ==========");
        List<Product> likeList = productDao.listProductByLikeName("键");
        System.out.println(likeList);

        // 6. 根据ID删除
        System.out.println("========== 删除产品 ==========");
        // 删除刚新增的产品：先查全部取最后一个 pid
        List<Product> products = productDao.listAllProduct();
        int lastPid = products.get(products.size() - 1).getPid();
        productDao.delProductById(lastPid);
        System.out.println("已删除 pid=" + lastPid);
        System.out.println(productDao.listAllProduct());
    }
}
