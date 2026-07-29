package zuoye3;

/**
 * 编程题1：反转字符串中每个单词的字符顺序，保留空格和单词顺序。
 * 示例：输入 "Let's take LeetCode contest"
 *       输出 "s'teL ekat edoCteeL tsetnoc"
 */
public class ReverseWords {

    public static String reverseWords(String s) {
        String[] words = s.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(new StringBuilder(words[i]).reverse());
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String s = "Let's take LeetCode contest";
        System.out.println(reverseWords(s));
    }
}
