package org.sakila.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * 控制台输入辅助：
 * - 必填数字/文本：空或非法则提示并重新输入
 * - 修改时回车 → 保留原值（非法则重输）
 * - 查询时回车 → 忽略该筛选条件（非法则重输）
 */
public final class InputHelper {

    private InputHelper() {
    }

    /** 必填整数：不能空，非法则重输 */
    public static int readRequiredInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("不能为空，请重新输入。");
                continue;
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数。");
            }
        }
    }

    /** 必填文本：不能空 */
    public static String readRequiredString(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (!s.trim().isEmpty()) {
                return s;
            }
            System.out.println("不能为空，请重新输入。");
        }
    }

    /** 必填金额/小数 */
    public static BigDecimal readRequiredDecimal(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("不能为空，请重新输入。");
                continue;
            }
            try {
                return new BigDecimal(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字。");
            }
        }
    }

    /** 可空整数：回车返回 null；非法则重输 */
    public static Integer readOptionalInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数，或直接回车跳过。");
            }
        }
    }

    /** 可空小数（Double）：回车返回 null；非法则重输 */
    public static Double readOptionalDouble(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字，或直接回车跳过。");
            }
        }
    }

    /** 可空小数：回车使用 defaultVal；非法则重输 */
    public static BigDecimal readOptionalDecimal(Scanner in, String prompt, BigDecimal defaultVal) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return defaultVal;
            }
            try {
                return new BigDecimal(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字，或直接回车使用默认值。");
            }
        }
    }

    /** 可空文本：回车返回 null（表示空） */
    public static String readOptionalString(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine();
        return s.isEmpty() ? null : s;
    }

    public static String keepString(Scanner in, String label, String oldVal) {
        System.out.print(label + "(当前: " + show(oldVal) + ", 回车跳过): ");
        String s = in.nextLine();
        return s.isEmpty() ? oldVal : s;
    }

    public static int keepInt(Scanner in, String label, int oldVal) {
        while (true) {
            System.out.print(label + "(当前: " + oldVal + ", 回车跳过): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return oldVal;
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数，或直接回车跳过。");
            }
        }
    }

    public static Integer keepInteger(Scanner in, String label, Integer oldVal) {
        while (true) {
            System.out.print(label + "(当前: " + show(oldVal) + ", 回车跳过): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return oldVal;
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数，或直接回车跳过。");
            }
        }
    }

    public static Double keepDouble(Scanner in, String label, Double oldVal) {
        while (true) {
            System.out.print(label + "(当前: " + show(oldVal) + ", 回车跳过): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return oldVal;
            }
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字，或直接回车跳过。");
            }
        }
    }

    public static BigDecimal keepDecimal(Scanner in, String label, BigDecimal oldVal) {
        while (true) {
            System.out.print(label + "(当前: " + show(oldVal) + ", 回车跳过): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return oldVal;
            }
            try {
                return new BigDecimal(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字，或直接回车跳过。");
            }
        }
    }

    /** 1=是 / 0=否，回车保留原值；其他非法值重输 */
    public static boolean keepActive(Scanner in, String label, boolean oldVal) {
        while (true) {
            System.out.print(label + "(当前: " + (oldVal ? "是" : "否") + ", 输入1是/0否, 回车跳过): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return oldVal;
            }
            if ("1".equals(s) || "0".equals(s)) {
                return "1".equals(s);
            }
            System.out.println("请输入 1 或 0，或直接回车跳过。");
        }
    }

    /** 查询条件：回车返回 null 表示忽略；非法则重输 */
    public static Integer filterInt(Scanner in, String label) {
        while (true) {
            System.out.print(label + "(回车忽略): ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数，或直接回车忽略。");
            }
        }
    }

    /** 查询条件：回车返回 null 表示忽略 */
    public static String filterString(Scanner in, String label) {
        System.out.print(label + "(回车忽略): ");
        String s = in.nextLine().trim();
        return s.isEmpty() ? null : s;
    }

    public static void printQueryTip() {
        System.out.println("说明：直接回车表示忽略该条件；全部回车则查询全部。");
    }

    /** 查询列表输出（超过10条分页），并附带去重后的关联表内容 */
    public static void printList(List<?> list, Scanner in) {
        RelationHelper.printWithRelations(list, in);
    }

    public static void printOne(Object obj) {
        System.out.println(obj == null ? "未找到" : obj);
    }

    private static String show(Object val) {
        return val == null ? "空" : String.valueOf(val);
    }
}
