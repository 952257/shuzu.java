package org.sakila.example;

import java.util.HashMap;
import java.util.Map;

/**
 * 相当于银行服务器
 * 负责业务流程
 */
public class BankServer {

    private Map<String, Account> accounts = new HashMap<>();

    private double atmCash = 100000;

    public static final double WITHDRAW_MAX = 5000;

    public BankServer() {
        accounts.put("1001", new Account("1001", "123456", 10000));
        accounts.put("1002", new Account("1002", "654321", 20000));
        accounts.put("1003", new Account("1003", "111111", 30000));
    }

    /**
     * 校验卡号
     * @param cardno 卡号
     * @return true 卡号正确 false 卡号错误
     */
    public boolean checkCardno(String cardno) {
        return accounts.containsKey(cardno);
    }

    /**
     * 账户是否已冻结
     */
    public boolean isFrozen(String cardno) {
        Account account = accounts.get(cardno);
        return account != null && account.getStatus() == 1;
    }

    /**
     * 校验密码
     * @param cardno 卡号
     * @param password 密码
     * @return true 密码正确 false 密码错误
     */
    public boolean checkPassowrd(String cardno, String password) {
        Account account = accounts.get(cardno);
        if (account == null) {
            return false;
        }
        return account.getPassword().equals(password);
    }

    /**
     * 密码错误一次；连续错满3次则冻结
     * @return 剩余可尝试次数，0表示已冻结
     */
    public int passwordFail(String cardno) {
        Account account = accounts.get(cardno);
        if (account == null) {
            return 0;
        }
        account.setFailCount(account.getFailCount() + 1);
        if (account.getFailCount() >= 3) {
            account.setStatus(1);//冻结
            return 0;
        }
        return 3 - account.getFailCount();
    }

    //登录成功后清零密码错误次数
    public void resetFailCount(String cardno) {
        Account account = accounts.get(cardno);
        if (account != null) {
            account.setFailCount(0);
        }
    }

    //查询余额
    public double queryBalance(String cardno) {
        return accounts.get(cardno).getBalance();
    }

    //校验金额是否为100的倍数且大于0
    public boolean isHundredMultiple(double money) {
        if (money <= 0) {
            return false;
        }
        long yuan = Math.round(money);
        return Math.abs(money - yuan) < 1e-9 && yuan % 100 == 0;
    }

    /**
     * 存款
     * @return null成功，否则为错误提示
     */
    public String deposit(String cardno, double money) {
        if (!isHundredMultiple(money)) {
            return "输入金额必须是100的整数，请从新输入";
        }
        Account account = accounts.get(cardno);
        account.setBalance(account.getBalance() + money);
        atmCash += money;
        return null;
    }

    /**
     * 取款
     * @return null成功，否则为错误提示
     */
    public String withdraw(String cardno, double money) {
        if (!isHundredMultiple(money)) {
            return "输入金额必须是100的整数，请从新输入";
        }
        if (money > WITHDRAW_MAX) {
            return "输入金额不能超过5000，请重新输入";
        }
        Account account = accounts.get(cardno);
        if (account.getBalance() < money) {
            return "账户余额不足，请重新输入";
        }
        if (atmCash < money) {
            return "该ATM机现金不足，请更换一台取款。";
        }
        account.setBalance(account.getBalance() - money);
        atmCash -= money;
        return null;
    }

    /**
     * 转账
     * @return null成功，否则为错误提示
     */
    public String transfer(String fromCardno, String toCardno, double money) {
        if (fromCardno.equals(toCardno)) {
            return "不能向自己的账户转账，请重新输入";
        }
        if (!accounts.containsKey(toCardno)) {
            return "对方卡号不存在，请重新输入";
        }
        if (isFrozen(toCardno)) {
            return "对方账户已冻结，无法转账";
        }
        if (!isHundredMultiple(money)) {
            return "输入金额必须是100的整数，请从新输入";
        }
        Account from = accounts.get(fromCardno);
        if (from.getBalance() < money) {
            return "账户余额不足，请重新输入";
        }
        Account to = accounts.get(toCardno);
        from.setBalance(from.getBalance() - money);
        to.setBalance(to.getBalance() + money);
        return null;
    }
}
