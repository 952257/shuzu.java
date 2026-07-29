package org.sakila.view;

import org.sakila.Controller;
import org.sakila.common.InputHelper;
import org.sakila.dao.AddressDao;
import org.sakila.entity.Address;

import java.util.Scanner;

public class AddressView implements View {

    Scanner in = new Scanner(System.in);
    AddressDao addressDao = new AddressDao();

    @Override
    public void indexWindow() {
        System.out.println("#########地址信息管理#########");
        System.out.println("请选择:");
        System.out.println("1.添加地址");
        System.out.println("2.修改地址");
        System.out.println("3.删除地址");
        System.out.println("4.查询地址");
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
        System.out.println("-----添加地址-----");
        Address address = new Address();
        address.setAddress(InputHelper.readRequiredString(in, "[必填] 请输入地址: "));
        address.setAddress2(InputHelper.readOptionalString(in, "[可空] 请输入地址第二行: "));
        address.setDistrict(InputHelper.readRequiredString(in, "[必填] 请输入地区: "));
        address.setCityId(InputHelper.readRequiredInt(in, "[必填] 请输入城市ID: "));
        address.setPostalCode(InputHelper.readOptionalString(in, "[可空] 请输入邮编: "));
        address.setPhone(InputHelper.readRequiredString(in, "[必填] 请输入电话: "));
        address.setLongitude(InputHelper.readOptionalDouble(in, "[可空] 请输入经度(默认0): "));
        address.setLatitude(InputHelper.readOptionalDouble(in, "[可空] 请输入纬度(默认0): "));
        addressDao.addAddress(address);
        System.out.println("添加成功");
    }

    public void updateWindow() {
        System.out.println("-----修改地址-----");
        System.out.println("说明：不想改的字段直接回车跳过。");
        int addressId = InputHelper.readRequiredInt(in, "请输入要修改的地址ID: ");
        Address old = addressDao.getAddressById(addressId);
        if (old == null) {
            System.out.println("地址不存在");
            return;
        }
        System.out.println("原信息: " + old);
        old.setAddress(InputHelper.keepString(in, "地址", old.getAddress()));
        old.setAddress2(InputHelper.keepString(in, "地址补充", old.getAddress2()));
        old.setDistrict(InputHelper.keepString(in, "地区", old.getDistrict()));
        old.setCityId(InputHelper.keepInt(in, "城市编号", old.getCityId()));
        old.setPostalCode(InputHelper.keepString(in, "邮政编码", old.getPostalCode()));
        old.setPhone(InputHelper.keepString(in, "电话", old.getPhone()));
        old.setLongitude(InputHelper.keepDouble(in, "经度", old.getLongitude()));
        old.setLatitude(InputHelper.keepDouble(in, "纬度", old.getLatitude()));
        addressDao.updateAddress(old);
        System.out.println("修改成功");
    }

    public void deleteWindow() {
        System.out.println("-----删除地址-----");
        int addressId = InputHelper.readRequiredInt(in, "请输入要删除的地址ID: ");
        Address old = addressDao.getAddressById(addressId);
        if (old == null) {
            System.out.println("地址不存在");
            return;
        }
        addressDao.deleteAddress(addressId);
        System.out.println("删除成功: " + old);
    }

    public void queryWindow() {
        System.out.println("-----查询地址-----");
        InputHelper.printQueryTip();
        Integer id = InputHelper.filterInt(in, "地址编号");
        String keyword = InputHelper.filterString(in, "地址/地区关键字");
        Integer cityId = InputHelper.filterInt(in, "城市编号");
        InputHelper.printList(addressDao.selectByCondition(id, keyword, cityId), in);
    }
}
