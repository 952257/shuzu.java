package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.PaymentDao;
import org.sakila.entity.Payment;

import java.util.Scanner;

public class PaymentView implements View {

    Scanner in = new Scanner(System.in);
    PaymentDao dao = new PaymentDao();

    @Override
    public void indexWindow() {
        System.out.println("#########支付信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加支付");
        System.out.println("2.修改支付");
        System.out.println("3.删除支付");
        System.out.println("4.查询支付");
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
        System.out.println("-----添加支付-----");
        Payment p = readPaymentForAdd();
        dao.add(p);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改支付-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入支付ID: ");
        Payment old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setCustomerId(InputHelper.keepInt(in, "客户编号", old.getCustomerId()));
        old.setStaffId(InputHelper.keepInt(in, "员工编号", old.getStaffId()));
        old.setRentalId(InputHelper.keepInteger(in, "租赁编号", old.getRentalId()));
        old.setAmount(InputHelper.keepDecimal(in, "支付金额", old.getAmount()));
        old.setPaymentDate(InputHelper.keepString(in, "支付时间", old.getPaymentDate()));
        old.setPaymentId(id);
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除支付-----");
        int id = InputHelper.readRequiredInt(in, "请输入支付ID: ");
        Payment old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询支付-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "支付编号");
        Integer customerId = InputHelper.filterInt(in, "客户编号");
        Integer staffId = InputHelper.filterInt(in, "员工编号");
        InputHelper.printList(dao.selectByCondition(id, customerId, staffId), in);
    }

    private Payment readPaymentForAdd() {
        Payment p = new Payment();
        p.setCustomerId(InputHelper.readRequiredInt(in, "[必填] 请输入客户ID: "));
        p.setStaffId(InputHelper.readRequiredInt(in, "[必填] 请输入员工ID: "));
        p.setRentalId(InputHelper.readOptionalInt(in, "[可空] 请输入租赁ID: "));
        p.setAmount(InputHelper.readRequiredDecimal(in, "[必填] 请输入金额: "));
        p.setPaymentDate(InputHelper.readRequiredString(in, "[必填] 请输入支付时间(yyyy-MM-dd HH:mm:ss): "));
        return p;
    }
}
