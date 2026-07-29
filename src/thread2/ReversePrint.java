package thread2;

/**
 * 编程题1：字符串“abcdefg”，每秒钟倒序打印一个字符
 */
public class ReversePrint {
    public static void main(String[] args) throws InterruptedException {
        String s = "abcdefg";
        for (int i = s.length() - 1; i >= 0; i--) {
            System.out.println(s.charAt(i));
            if (i > 0) {
                Thread.sleep(1000);
            }
        }
    }
}
