package org.sakila;

import org.sakila.common.DBInfo;
import org.sakila.view.*;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    static Map<String, View> viewMap = new HashMap<>();

    static {
        viewMap.put("index", new IndexView());
        viewMap.put("category", new CategoryView());
        viewMap.put("payment", new PaymentView());
        viewMap.put("language", new LanguageView());
        viewMap.put("film", new FilmView());
        viewMap.put("customer", new CustomerView());
        viewMap.put("staff", new StaffView());
        viewMap.put("country", new CountryView());
        viewMap.put("city", new CityView());
        viewMap.put("store", new StoreView());
        viewMap.put("address", new AddressView());
        viewMap.put("rental", new RentalView());
    }

    public static void redirect(String viewName) {
        viewMap.get(viewName).indexWindow();
    }

    public static void main(String[] args) {
        System.out.println("正在检测数据库连接...");
        String error = DBInfo.checkConnection();
        if (error != null) {
            System.out.println("========================================");
            System.out.println("数据库连接失败，无法进入系统管理界面。");
            System.out.println("请检查：");
            System.out.println("  1. MySQL 服务是否已启动");
            System.out.println("  2. 库是否已创建并导入数据");
            System.out.println("  3. DBInfo 中的地址、用户名、密码是否正确");
            System.out.println("  4. 是否已添加 mysql-connector-j 驱动");
            System.out.println("失败原因: " + error);
            System.out.println("========================================");
            return;
        }
        System.out.println("数据库连接成功。");
        redirect("index");
    }
}
