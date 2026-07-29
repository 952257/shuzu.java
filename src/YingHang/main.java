package YingHang;

public class main {
    public static void main(String[] args) {
        Account account = new Account(1001, "张三", 1000);

        account.showBalance();
        account.deposit(500);
        account.showBalance();
        account.withdraw(300);
        account.showBalance();
        account.withdraw(2000);
    }
}
