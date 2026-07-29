package YingHang;

public class Account {
    int accountNo;
    String accountName;
    double balance;

    public Account(int accountNo, String accountName, double balance) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.balance = balance;
    }

    public void deposit(double money) {
        balance += money;
        System.out.println(accountName + "存款：" + money + "元");
    }

    public void withdraw(double money) {
        if (balance >= money) {
            balance -= money;
            System.out.println(accountName + "取款：" + money + "元");
        } else {
            System.out.println("余额不足，取款失败！");
        }
    }

    public void showBalance() {
        System.out.println(accountName + "当前余额：" + balance + "元");
    }
}
