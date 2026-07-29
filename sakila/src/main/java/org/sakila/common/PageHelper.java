package org.sakila.common;

import java.util.List;
import java.util.Scanner;

/**
 * 查询结果分页：超过 10 条时分页显示
 * 1=上一页  2=下一页  3=退出
 */
public final class PageHelper {

    public static final int PAGE_SIZE = 10;

    private PageHelper() {
    }

    public static void printPaged(List<?> list, Scanner in) {
        if (list == null || list.isEmpty()) {
            System.out.println("共 0 条");
            return;
        }
        int total = list.size();
        if (total <= PAGE_SIZE) {
            for (Object obj : list) {
                System.out.println(obj);
            }
            System.out.println("共 " + total + " 条");
            return;
        }

        int totalPages = (total + PAGE_SIZE - 1) / PAGE_SIZE;
        int page = 1;
        while (true) {
            int from = (page - 1) * PAGE_SIZE;
            int to = Math.min(from + PAGE_SIZE, total);
            System.out.println("========== 第 " + page + " 页（共 " + totalPages + " 页，共 " + total + " 条） ==========");
            for (int i = from; i < to; i++) {
                System.out.println(list.get(i));
            }
            System.out.println("----------------------------------------");
            System.out.println("当前第 " + page + " 页，共 " + totalPages + " 页");
            System.out.println("1.上一页  2.下一页  3.退出显示");
            System.out.print("请选择: ");
            String select = in.nextLine().trim();
            switch (select) {
                case "1":
                    if (page <= 1) {
                        System.out.println("已经是第一页");
                    } else {
                        page--;
                    }
                    break;
                case "2":
                    if (page >= totalPages) {
                        System.out.println("已经是最后一页");
                    } else {
                        page++;
                    }
                    break;
                case "3":
                    System.out.println("已退出显示");
                    return;
                default:
                    System.out.println("输入有误，请输入 1/2/3");
            }
        }
    }
}
