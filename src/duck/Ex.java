package duck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ex {
    static void main(String[] args) {
        List<Duck> list = new ArrayList<>();
        list.add(new Duck("唐老鸭","公",10.2));
        list.add(new Duck("王小鸭","母",5.8));
        list.add(new Duck("张老鸭","母",8.5));
        list.add(new Duck("唐小鸭","母",6.6));
        list.add(new Duck("李老鸭"	,"公",7.8));
        list.add(new Duck("李小鸭","公",3.3));
        list.add(new Duck("张小鸭","母",2.5));
        list.add(new Duck("唐大鸭","公",6.8));
        list.add(new Duck("张老鸭", "母",7.7));

//      使用多种方法遍历该List；
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("-------------------------");
        for (Duck duck : list) {
            System.out.println(duck);
        }
        System.out.println("-------------------------");
        list.forEach(System.out::println);
        System.out.println("-------------------------");
//      判断该List中是公鸭多还是母鸭多;
//      int[] maleCount = new int[1];
//      list.forEach(duck -> {
//        if(duck.getSex().equals("公"))
//          maleCount[0]++;
//      });
        int maleCount = 0;
        for (Duck duck : list) {
            if(duck.getSex().equals("公"))
                maleCount++;
            else maleCount--;
        }
        System.out.println(maleCount > 0 ? "公鸭多" : (maleCount < 0 ? "母鸭多" : "一样多"));

        System.out.println("-------------------------");


//      判断该List中是李姓鸭子多还是王姓鸭子多；
        int liCount = 0;
        int wangCount = 0;
        for (Duck duck : list) {
            if(duck.getName().startsWith("李"))
                liCount++;
            else if(duck.getName().startsWith("王"))
                wangCount++;
        }
        System.out.println(liCount > wangCount ? "李姓多" : (liCount < wangCount ? "王姓多" : "一样多"));

        //      计算所有鸭子的平均体重
        double totalWeight = 0;
        for (Duck duck : list) {
            totalWeight += duck.getWeight();
        }
        System.out.println("所有鸭子的平均体重为：" + totalWeight / list.size());

        //
//              删除List中所有的老鸭
        list.removeIf(duck -> duck.getName().endsWith("老鸭"));

        //冒泡排序 按照体重
//      for (int i = 0; i < list.size()-1; i++) {
//        for (int j = 0; j < list.size()-1-i; j++) {
//          if(list.get(j).getWeight() > list.get(j+1).getWeight()){
//            Duck temp = list.get(j);
//            list.set(j, list.get(j+1));
//            list.set(j+1, temp);
//          }
//        }
//      }
//      System.out.println(list);

        list.sort(Comparator.comparing(Duck::getWeight).reversed());
        System.out.println(list);
    }
}
