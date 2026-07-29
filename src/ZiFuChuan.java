public class ZiFuChuan {
    public static void main(String[] args) {
        String str1 = "sadewrdsfs";
        System.out.println("第8个字符是：" + str1.charAt(7));

        String email = "java@oracle.com";
        System.out.println("是否包含@：" + email.contains("@"));

        int start = email.indexOf("@") + 1;
        int end = email.indexOf(".");
        String company = email.substring(start, end);
        System.out.println("@与.之间的字符串是：" + company);

        String str2 = "abcdefg";
        String reverse = new StringBuilder(str2).reverse().toString();
        System.out.println("倒序后的字符串是：" + reverse);

        String str3 = "abcdefgabdcdefgabcddefg";
        int count = str3.length() - str3.replace("d", "").length();
        System.out.println("字符d的个数是：" + count);

        String str4 = "11#2#333#444#55";
        String[] nums = str4.split("#");
        int sum = 0;
        for (String num : nums) {
            sum += Integer.parseInt(num);
        }
        System.out.println("各个数字之和是：" + sum);

        String str5 = "abcdefg";
        if (str5.startsWith("abc")) {
            String newStr = str5.substring(3) + str5.substring(0, 3).toUpperCase();
            System.out.println("处理后的字符串是：" + newStr);
        }
    }
}
