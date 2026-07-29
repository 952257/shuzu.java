package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.StoreDao;
import org.sakila.entity.Store;

import java.util.Scanner;

public class StoreView implements View {

    Scanner in = new Scanner(System.in);
    StoreDao dao = new StoreDao();

    @Override
    public void indexWindow() {
        System.out.println("#########商店信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加商店");
        System.out.println("2.修改商店");
        System.out.println("3.删除商店");
        System.out.println("4.查询商店");
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
        System.out.println("-----添加商店-----");
        Store store = new Store();
        store.setManagerStaffId(InputHelper.readRequiredInt(in, "[必填] 请输入经理员工ID: "));
        store.setAddressId(InputHelper.readRequiredInt(in, "[必填] 请输入地址ID: "));
        dao.add(store);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改商店-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入商店ID: ");
        Store old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setManagerStaffId(InputHelper.keepInt(in, "经理员工编号", old.getManagerStaffId()));
        old.setAddressId(InputHelper.keepInt(in, "地址编号", old.getAddressId()));
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除商店-----");
        int id = InputHelper.readRequiredInt(in, "请输入商店ID: ");
        Store old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询商店-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "商店编号");
        Integer managerId = InputHelper.filterInt(in, "店长员工编号");
        InputHelper.printList(dao.selectByCondition(id, managerId), in);
    }
}
