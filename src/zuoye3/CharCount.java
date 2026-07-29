package zuoye3;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 编程题2：统计字符串中每个字符出现次数（忽略大小写），按字符顺序输出。
 * 示例：输入 "Hello World"
 *       输出 d:1, e:1, h:1, l:3, o:2, r:1, w:1
 */
public class CharCount {

    public static String countChars(String s) {
        Map<Character, Integer> map = new TreeMap<>();
        for (char c : s.toLowerCase().toCharArray()) {
            if (c == ' ') {
                continue; // 示例中空格不统计
            }
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (!first) {
                result.append(", ");
            }
            result.append(entry.getKey()).append(":").append(entry.getValue());
            first = false;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("请输入字符串: ");
        String s = in.nextLine();
        System.out.println(countChars(s));
        in.close();
    }
}