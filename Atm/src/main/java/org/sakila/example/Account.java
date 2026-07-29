package org.sakila.example;

/**
 * 相当于银行账户
 */
public class Account {

    private String id;

    private String password;

    //余额
    private double balance;

    private int status;//0-正常 1-冻结

    //连续密码错误次数
    private int failCount;

    public Account(String id, String password, double balance) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.status = 0;
        this.failCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
}
