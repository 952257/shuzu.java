package org.sakila.example;

import java.util.Scanner;

/**
 * 相当于ATM机 客户端
 * 负责跟用户交互
 */
public class ATM {

    Scanner in = new Scanner(System.in);

    BankServer bankServer = new BankServer();

    //当前登录卡号
    private String currentCardno;

    //首页
    public void indexWindow() {
        System.out.println("-------------中国银行ATM机------------");
        currentCardno = null;
        cardnoWindow();
    }

    //查询余额
    public void queryBalanceWindow() {
        double balance = bankServer.queryBalance(currentCardno);
        System.out.println("您的账户余额为：" + balance + " 元");
        printReceipt("查询余额", 0, balance);
        afterTrade();
    }

    //取款
    public void withdrawWindow() {
        System.out.print("请输入取款金额（须为100的倍数，单笔不超过5000，输入0取消）：");
        double money = readAmount();
        if (money == 0) {
            System.out.println("已取消取款。");
            menuWindow();
            return;
        }
        if (money < 0) {
            System.out.println("输入无效，请重新输入！");
            withdrawWindow();
            return;
        }
        String err = bankServer.withdraw(currentCardno, money);
        if (err != null) {
            System.out.println(err);
            if (err.contains("ATM机现金不足")) {
                menuWindow();
                return;
            }
            withdrawWindow();
            return;
        }
        System.out.println("请取走现金：" + money + " 元");
        double balance = bankServer.queryBalance(currentCardno);
        System.out.println("取款成功！当前余额：" + balance + " 元");
        printReceipt("取款", money, balance);
        afterTrade();
    }

    //存款
    public void depositWindow() {
        System.out.print("请输入存款金额（须为100的倍数，输入0取消）：");
        double money = readAmount();
        if (money == 0) {
            System.out.println("已取消存款。");
            menuWindow();
            return;
        }
        if (money < 0) {
            System.out.println("输入无效，请重新输入！");
            depositWindow();
            return;
        }
        String err = bankServer.deposit(currentCardno, money);
        if (err != null) {
            System.out.println(err);
            depositWindow();
            return;
        }
        double balance = bankServer.queryBalance(currentCardno);
        System.out.println("存款成功！当前余额：" + balance + " 元");
        printReceipt("存款", money, balance);
        afterTrade();
    }

    //转账
    public void transferWindow() {
        System.out.print("请输入对方卡号（输入0取消）：");
        String toCardno = in.nextLine().trim();
        if ("0".equals(toCardno)) {
            System.out.println("已取消转账。");
            menuWindow();
            return;
        }
        System.out.print("请输入转账金额（须为100的倍数）：");
        double money = readAmount();
        if (money <= 0) {
            System.out.println("输入无效，请重新输入！");
            transferWindow();
            return;
        }
        String err = bankServer.transfer(currentCardno, toCardno, money);
        if (err != null) {
            System.out.println(err);
            transferWindow();
            return;
        }
        double balance = bankServer.queryBalance(currentCardno);
        System.out.println("转账成功！当前余额：" + balance + " 元");
        printReceipt("转账至" + toCardno, money, balance);
        afterTrade();
    }

    public void menuWindow() {
        System.out.println("请选择:1.查询余额 2.取款 3.存款 4.转账 5.退出");
        String select = in.nextLine();
        switch (select) {
            case "1" -> queryBalanceWindow();
            case "2" -> withdrawWindow();
            case "3" -> depositWindow();
            case "4" -> transferWindow();
            case "5" -> System.out.println("请取走您的银行卡，欢迎下次使用！");
            default -> {
                System.out.println("输入错误，请重新输入");
                menuWindow();
            }
        }
    }

    /**
     * 密码连错三次会冻结，该卡暂时不可用
     * @param cardno
     */
    public void passwordWindow(String cardno) {
        if (bankServer.isFrozen(cardno)) {
            System.out.println("该卡已冻结，暂时不可用，请联系银行。");
            indexWindow();
            return;
        }
        System.out.println("请输入密码:");
        String password = in.nextLine();
        //密码须为6位整数
        if (!password.matches("\\d{6}")) {
            System.out.println("密码必须是6位整数！");
            handlePasswordFail(cardno);
            return;
        }
        boolean result = bankServer.checkPassowrd(cardno, password);
        if (result) {
            bankServer.resetFailCount(cardno);
            currentCardno = cardno;
            System.out.println("登录成功！");
            menuWindow();
        } else {
            System.out.println("密码错误，请重新输入");
            handlePasswordFail(cardno);
        }
    }

    //处理密码错误：累计次数，满3次冻结
    private void handlePasswordFail(String cardno) {
        int remain = bankServer.passwordFail(cardno);
        if (remain == 0) {
            System.out.println("密码连续错误3次，该卡已冻结，暂时不可用！");
            indexWindow();
        } else {
            System.out.println("还可尝试 " + remain + " 次");
            passwordWindow(cardno);
        }
    }

    public void cardnoWindow() {
        System.out.println("请输入卡号:");
        if (!in.hasNextLine()) {
            return;
        }
        String cardno = in.nextLine();
        //校验卡号
        boolean result = bankServer.checkCardno(cardno);
        if (result) {
            if (bankServer.isFrozen(cardno)) {
                System.out.println("该卡已冻结，暂时不可用，请联系银行。");
                cardnoWindow();
                return;
            }
            passwordWindow(cardno);
        }
        //卡号不对
        else {
            System.out.println("卡号错误，请重新输入");
            cardnoWindow();
        }
    }

    //交易后：是否继续
    private void afterTrade() {
        System.out.print("是否继续办理业务？(1-继续 / 0-退出)：");
        String choice = in.nextLine().trim();
        if ("1".equals(choice)) {
            menuWindow();
        } else {
            System.out.println("请取走您的银行卡，欢迎下次使用！");
        }
    }

    //是否打印凭条
    private void printReceipt(String type, double amount, double balance) {
        System.out.print("是否打印凭条？(1-是 / 0-否)：");
        if ("1".equals(in.nextLine().trim())) {
            System.out.println("============ 交易凭条 ============");
            System.out.println("卡号：****" + currentCardno.substring(Math.max(0, currentCardno.length() - 2)));
            System.out.println("交易类型：" + type);
            if (amount > 0) {
                System.out.println("交易金额：" + amount + " 元");
            }
            System.out.println("账户余额：" + balance + " 元");
            System.out.println("================================");
        }
    }

    //读取金额，非法输入返回-1
    private double readAmount() {
        String line = in.nextLine().trim();
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
