package duck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Ex2 {
    static void main(String[] args) {
        List<Duck> list = new ArrayList<>();
        list.add(new Duck("唐老鸭", "公", 10.2));
        list.add(new Duck("王小鸭", "母", 5.8));
        list.add(new Duck("张老鸭", "母", 8.5));
        list.add(new Duck("唐小鸭", "母", 6.6));
        list.add(new Duck("李老鸭", "公", 7.8));
        list.add(new Duck("李小鸭", "公", 3.3));
        list.add(new Duck("张小鸭", "母", 2.5));
        list.add(new Duck("唐大鸭", "公", 6.8));
        list.add(new Duck("张老鸭", "母", 7.7));

        // 将List中所有Duck对象放入HashMap，以name为key，Duck对象为value
        HashMap<String, Duck> map = new HashMap<>();
        for (Duck duck : list) {
            map.put(duck.getName(), duck);
        }

        // 使用多种方法遍历HashMap，显示key和value
        for (String key : map.keySet()) {
            System.out.println("key: " + key + ", value: " + map.get(key));
        }
        System.out.println("-------------------------");
        for (Map.Entry<String, Duck> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
        System.out.println("-------------------------");
        Iterator<Map.Entry<String, Duck>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Duck> entry = iterator.next();
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
        System.out.println("-------------------------");
        map.forEach((key, value) -> System.out.println("key: " + key + ", value: " + value));
        System.out.println("-------------------------");

        // 判断HashMap中是公鸭多还是母鸭多
        int maleCount = 0;
        for (Duck duck : map.values()) {
            if (duck.getSex().equals("公"))
                maleCount++;
            else maleCount--;
        }
        System.out.println(maleCount > 0 ? "公鸭多" : (maleCount < 0 ? "母鸭多" : "一样多"));
        System.out.println("-------------------------");

        // 判断HashMap中是李姓鸭子多还是王姓鸭子多
        int liCount = 0;
        int wangCount = 0;
        for (Duck duck : map.values()) {
            if (duck.getName().startsWith("李"))
                liCount++;
            else if (duck.getName().startsWith("王"))
                wangCount++;
        }
        System.out.println(liCount > wangCount ? "李姓多" : (liCount < wangCount ? "王姓多" : "一样多"));
        System.out.println("-------------------------");

        // 删除HashMap中所有的老鸭
        map.entrySet().removeIf(entry -> entry.getKey().endsWith("老鸭"));
        System.out.println(map);
    }
}
