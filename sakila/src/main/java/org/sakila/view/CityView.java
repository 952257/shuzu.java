package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.CityDao;
import org.sakila.entity.City;

import java.util.Scanner;

/**
 * 城市信息管理（仿教学项目 CityView，并按本项目交互规范实现）
 */
public class CityView implements View {

    Scanner in = new Scanner(System.in);
    CityDao cityDao = new CityDao();

    @Override
    public void indexWindow() {
        System.out.println("#########城市信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加城市");
        System.out.println("2.修改城市");
        System.out.println("3.删除城市");
        System.out.println("4.查询城市");
        System.out.println("5.返回上一级菜单");
        String select = in.nextLine();
        switch (select) {
            case "1":
                addWindow();
                break;
            case "2":
                updateWindow();
                break;
            case "3":
                deleteWindow();
                break;
            case "4":
                queryWindow();
                break;
            case "5":
                Controller.redirect("index");
                return;
            default:
                System.out.println("输入有误，请重新选择");
        }
        indexWindow();
    }

    public void addWindow() {
        System.out.println("-----添加城市-----");
        City city = new City();
        city.setCity(InputHelper.readRequiredString(in, "[必填] 请输入城市名称: "));
        city.setCountryId(InputHelper.readRequiredInt(in, "[必填] 请输入国家ID: "));
        cityDao.addCity(city);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改城市-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int cityId = InputHelper.readRequiredInt(in, "请输入要修改的城市ID: ");
        City old = cityDao.getById(cityId);
        if (old == null) {
            System.out.println("城市不存在");
            return;
        }
        System.out.println("原信息: " + old);
        old.setCity(InputHelper.keepString(in, "城市名称", old.getCity()));
        old.setCountryId(InputHelper.keepInt(in, "国家编号", old.getCountryId()));
        cityDao.updateCity(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除城市-----");
        int cityId = InputHelper.readRequiredInt(in, "请输入要删除的城市ID: ");
        City old = cityDao.getById(cityId);
        if (old == null) {
            System.out.println("城市不存在");
            return;
        }
        cityDao.deleteCity(cityId);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询城市-----");
        InputHelper.printQueryTip();
        Integer cityId = InputHelper.filterInt(in, "城市编号");
        String cityKey = InputHelper.filterString(in, "城市名称关键字");
        Integer countryId = InputHelper.filterInt(in, "国家编号");
        InputHelper.printList(cityDao.selectByCondition(cityId, cityKey, countryId), in);
    }
}
