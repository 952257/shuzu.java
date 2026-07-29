import java.util.Scanner;

public class FangFaZuoYe {
    public static void main(String[] args) {
        int ji = qiuJi(3, 5);
        System.out.println("两个整数的乘积：" + ji);

        boolean shiOuShu = panDuanOuShu(6);
        System.out.println("这个整数是否为偶数：" + shiOuShu);

        int[] shuZu = {10, 20, 30, 40, 50};
        double pingJunZhi = qiuPingJunZhi(shuZu);
        System.out.println("数组的平均值：" + pingJunZhi);

        int zuiDaZhi = qiuZuiDaZhi(shuZu);
        System.out.println("数组中的最大值：" + zuiDaZhi);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要查询的书名：");
            String bookName = scanner.next();

            boolean chaDaoLe = findBook(bookName);
            if (chaDaoLe) {
                return;
            }
        }
    }

    public static int qiuJi(int num1, int num2) {
        return num1 * num2;
    }

    public static boolean panDuanOuShu(int num) {
        return num % 2 == 0;
    }

    public static double qiuPingJunZhi(int[] shuZu) {
        int he = 0;
        for (int i = 0; i < shuZu.length; i++) {
            he += shuZu[i];
        }
        return (double) he / shuZu.length;
    }

    public static int qiuZuiDaZhi(int[] shuZu) {
        int zuiDaZhi = shuZu[0];
        for (int i = 1; i < shuZu.length; i++) {
            if (shuZu[i] > zuiDaZhi) {
                zuiDaZhi = shuZu[i];
            }
        }
        return zuiDaZhi;
    }

    public static boolean findBook(String bookName) {
        String[] shuMing = {"西游记", "水浒传", "三国演义", "红楼梦", "Java基础"};

        for (int i = 0; i < shuMing.length; i++) {
            if (shuMing[i].equals(bookName)) {
                System.out.println("书籍已查询到！");
                return true;
            }
        }
        System.out.println("书籍不存在，请重新输入！");
        return false;
    }
}
