package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.CountryDao;
import org.sakila.entity.Country;

import java.util.Scanner;

public class CountryView implements View {

    Scanner in = new Scanner(System.in);
    CountryDao dao = new CountryDao();

    @Override
    public void indexWindow() {
        System.out.println("#########国家信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加国家");
        System.out.println("2.修改国家");
        System.out.println("3.删除国家");
        System.out.println("4.查询国家");
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
        System.out.println("-----添加国家-----");
        Country c = new Country();
        c.setCountry(InputHelper.readRequiredString(in, "[必填] 请输入国家名称: "));
        dao.addCountry(c);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改国家-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入国家ID: ");
        Country old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setCountry(InputHelper.keepString(in, "国家名称", old.getCountry()));
        dao.updateCountry(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除国家-----");
        int id = InputHelper.readRequiredInt(in, "请输入国家ID: ");
        Country old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.deleteCountry(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询国家-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "国家编号");
        String keyword = InputHelper.filterString(in, "国家名称关键字");
        InputHelper.printList(dao.selectByCondition(id, keyword), in);
    }
}
