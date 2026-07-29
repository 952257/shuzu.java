package org.sakila.view;

import org.sakila.Controller;

import java.util.Scanner;

/**
 * 系统首页
 */
public class IndexView implements View {

    Scanner in = new Scanner(System.in);

    @Override
    public void indexWindow() {
        System.out.println("-------------Sakila在线影院管理系统------------");
        System.out.println("请选择:");
        System.out.println("1.类别管理      2.支付管理");
        System.out.println("3.语言管理      4.影片管理");
        System.out.println("5.客户管理      6.员工管理");
        System.out.println("7.国家管理      8.城市管理");
        System.out.println("9.商店管理     10.地址管理");
        System.out.println("11.租赁管理");
        System.out.println("0.退出系统");
        System.out.print("请选择: ");
        String select = in.nextLine();
        switch (select) {
            case "1": Controller.redirect("category"); break;
            case "2": Controller.redirect("payment"); break;
            case "3": Controller.redirect("language"); break;
            case "4": Controller.redirect("film"); break;
            case "5": Controller.redirect("customer"); break;
            case "6": Controller.redirect("staff"); break;
            case "7": Controller.redirect("country"); break;
            case "8": Controller.redirect("city"); break;
            case "9": Controller.redirect("store"); break;
            case "10": Controller.redirect("address"); break;
            case "11": Controller.redirect("rental"); break;
            case "0":
                System.out.println("感谢使用，再见！");
                break;
            default:
                System.out.println("输入有误，请重新选择");
                indexWindow();
        }
    }
}
