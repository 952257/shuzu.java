package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.RentalDao;
import org.sakila.entity.Rental;

import java.util.Scanner;

public class RentalView implements View {

    Scanner in = new Scanner(System.in);
    RentalDao dao = new RentalDao();

    @Override
    public void indexWindow() {
        System.out.println("#########租赁信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加租赁");
        System.out.println("2.修改租赁");
        System.out.println("3.删除租赁");
        System.out.println("4.查询租赁");
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
        System.out.println("-----添加租赁-----");
        Rental r = readRentalForAdd();
        dao.add(r);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改租赁-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入租赁ID: ");
        Rental old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setRentalDate(InputHelper.keepString(in, "租赁时间", old.getRentalDate()));
        old.setInventoryId(InputHelper.keepInt(in, "库存编号", old.getInventoryId()));
        old.setCustomerId(InputHelper.keepInt(in, "客户编号", old.getCustomerId()));
        old.setReturnDate(InputHelper.keepString(in, "归还时间", old.getReturnDate()));
        old.setStaffId(InputHelper.keepInt(in, "员工编号", old.getStaffId()));
        old.setRentalId(id);
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除租赁-----");
        int id = InputHelper.readRequiredInt(in, "请输入租赁ID: ");
        Rental old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询租赁-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "租赁编号");
        Integer customerId = InputHelper.filterInt(in, "客户编号");
        Integer staffId = InputHelper.filterInt(in, "员工编号");
        InputHelper.printList(dao.selectByCondition(id, customerId, staffId), in);
    }

    private Rental readRentalForAdd() {
        Rental r = new Rental();
        r.setRentalDate(InputHelper.readRequiredString(in, "[必填] 请输入租赁时间(yyyy-MM-dd HH:mm:ss): "));
        r.setInventoryId(InputHelper.readRequiredInt(in, "[必填] 请输入库存ID: "));
        r.setCustomerId(InputHelper.readRequiredInt(in, "[必填] 请输入客户ID: "));
        r.setReturnDate(InputHelper.readOptionalString(in, "[可空] 请输入归还时间(yyyy-MM-dd HH:mm:ss): "));
        r.setStaffId(InputHelper.readRequiredInt(in, "[必填] 请输入员工ID: "));
        return r;
    }
}
