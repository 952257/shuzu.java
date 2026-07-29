package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.StaffDao;
import org.sakila.entity.Staff;

import java.util.Scanner;

/**
 * 员工管理视图层，提供控制台交互界面。
 * 功能：添加、修改、删除、按条件查询员工。
 * 查询结果自动分页并展示关联的地址、商店信息。
 */
public class StaffView implements View {

    Scanner in = new Scanner(System.in);
    StaffDao dao = new StaffDao();

    @Override
    public void indexWindow() {
        System.out.println("#########员工信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加员工");
        System.out.println("2.修改员工");
        System.out.println("3.删除员工");
        System.out.println("4.查询员工");
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
        System.out.println("-----添加员工-----");
        Staff s = readStaffForAdd();
        dao.add(s);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改员工-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入员工ID: ");
        Staff old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setFirstName(InputHelper.keepString(in, "名", old.getFirstName()));
        old.setLastName(InputHelper.keepString(in, "姓", old.getLastName()));
        old.setAddressId(InputHelper.keepInt(in, "地址编号", old.getAddressId()));
        old.setEmail(InputHelper.keepString(in, "邮箱", old.getEmail()));
        old.setStoreId(InputHelper.keepInt(in, "所属商店编号", old.getStoreId()));
        old.setActive(InputHelper.keepActive(in, "是否在职", old.isActive()));
        old.setUsername(InputHelper.keepString(in, "用户名", old.getUsername()));
        old.setPassword(InputHelper.keepString(in, "密码", old.getPassword()));
        old.setStaffId(id);
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除员工-----");
        int id = InputHelper.readRequiredInt(in, "请输入员工ID: ");
        Staff old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询员工-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "员工编号");
        String keyword = InputHelper.filterString(in, "姓名/用户名关键字");
        Integer storeId = InputHelper.filterInt(in, "所属商店编号");
        InputHelper.printList(dao.selectByCondition(id, keyword, storeId), in);
    }

    private Staff readStaffForAdd() {
        Staff s = new Staff();
        s.setFirstName(InputHelper.readRequiredString(in, "[必填] 请输入名: "));
        s.setLastName(InputHelper.readRequiredString(in, "[必填] 请输入姓: "));
        s.setAddressId(InputHelper.readRequiredInt(in, "[必填] 请输入地址ID: "));
        s.setEmail(InputHelper.readOptionalString(in, "[可空] 请输入邮箱: "));
        s.setStoreId(InputHelper.readRequiredInt(in, "[必填] 请输入商店ID: "));
        System.out.print("[可空] 是否在职(1是0否，默认1): ");
        String active = in.nextLine().trim();
        s.setActive(active.isEmpty() || "1".equals(active));
        s.setUsername(InputHelper.readRequiredString(in, "[必填] 请输入用户名: "));
        s.setPassword(InputHelper.readOptionalString(in, "[可空] 请输入密码: "));
        return s;
    }
}
