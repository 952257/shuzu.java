package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.CustomerDao;
import org.sakila.entity.Customer;

import java.util.Scanner;

/**
 * 客户管理视图层，提供控制台交互界面。
 * 功能：添加、修改、删除、按条件查询客户。
 * 查询结果自动分页并展示关联的商店、地址信息。
 */
public class CustomerView implements View {

    Scanner in = new Scanner(System.in);
    CustomerDao dao = new CustomerDao();

    @Override
    public void indexWindow() {
        System.out.println("#########客户信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加客户");
        System.out.println("2.修改客户");
        System.out.println("3.删除客户");
        System.out.println("4.查询客户");
        System.out.println("5.返回上一级菜单");
        String select = in.nextLine();
        switch (select) {
            case "1": addWindow(); break;
            case "2": updateWindow(); break;
            case "3": deleteWindow(); break;
            case "4": queryWindow(); break;
            case "5": Controller.redirect("index"); return;
            default: System.out.println("输入有误，请重新选择");
        }
        indexWindow();
    }

    public void addWindow() {
        System.out.println("-----添加客户-----");
        System.out.println("提示：create_date（注册时间）由系统自动填写，无需输入。");
        Customer c = readCustomerForAdd();
        dao.add(c);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改客户-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入客户ID: ");
        Customer old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setStoreId(InputHelper.keepInt(in, "所属商店编号", old.getStoreId()));
        old.setFirstName(InputHelper.keepString(in, "名", old.getFirstName()));
        old.setLastName(InputHelper.keepString(in, "姓", old.getLastName()));
        old.setEmail(InputHelper.keepString(in, "邮箱", old.getEmail()));
        old.setAddressId(InputHelper.keepInt(in, "地址编号", old.getAddressId()));
        old.setActive(InputHelper.keepActive(in, "是否激活", old.isActive()));
        old.setCustomerId(id);
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除客户-----");
        int id = InputHelper.readRequiredInt(in, "请输入客户ID: ");
        Customer old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询客户-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "客户编号");
        String keyword = InputHelper.filterString(in, "姓名/邮箱关键字");
        Integer storeId = InputHelper.filterInt(in, "所属商店编号");
        InputHelper.printList(dao.selectByCondition(id, keyword, storeId), in);
    }

    private Customer readCustomerForAdd() {
        Customer c = new Customer();
        c.setStoreId(InputHelper.readRequiredInt(in, "[必填] 请输入商店ID: "));
        c.setFirstName(InputHelper.readRequiredString(in, "[必填] 请输入名: "));
        c.setLastName(InputHelper.readRequiredString(in, "[必填] 请输入姓: "));
        c.setEmail(InputHelper.readOptionalString(in, "[可空] 请输入邮箱: "));
        c.setAddressId(InputHelper.readRequiredInt(in, "[必填] 请输入地址ID: "));
        System.out.print("[可空] 是否活跃(1是0否，默认1): ");
        String active = in.nextLine().trim();
        c.setActive(active.isEmpty() || "1".equals(active));
        return c;
    }
}
