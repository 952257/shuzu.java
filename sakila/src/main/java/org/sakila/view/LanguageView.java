package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.LanguageDao;
import org.sakila.entity.Language;

import java.util.Scanner;

public class LanguageView implements View {

    Scanner in = new Scanner(System.in);
    LanguageDao dao = new LanguageDao();

    @Override
    public void indexWindow() {
        System.out.println("#########语言信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加语言");
        System.out.println("2.修改语言");
        System.out.println("3.删除语言");
        System.out.println("4.查询语言");
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
        System.out.println("-----添加语言-----");
        Language obj = new Language();
        obj.setName(InputHelper.readRequiredString(in, "[必填] 请输入语言名称: "));
        dao.add(obj);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改语言-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int id = InputHelper.readRequiredInt(in, "请输入语言ID: ");
        Language old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        System.out.println("原信息: " + old);
        old.setName(InputHelper.keepString(in, "语言名称", old.getName()));
        dao.update(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除语言-----");
        int id = InputHelper.readRequiredInt(in, "请输入语言ID: ");
        Language old = dao.getById(id);
        if (old == null) { System.out.println("不存在"); return; }
        dao.delete(id);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询语言-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "语言编号");
        String keyword = InputHelper.filterString(in, "语言名称关键字");
        InputHelper.printList(dao.selectByCondition(id, keyword), in);
    }
}
