package ChaZhao2.controller;

import ChaZhao2.entity.Duck;
import ChaZhao2.service.DuckService;
import ChaZhao2.service.impl.DuckServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class DuckController {
    public static ArrayList<Duck> list = new ArrayList<>();
    private static DuckService duckService;

    static {
        duckService = new DuckServiceImpl();
        initDuckList(list);
    }

    /**
     * 按照表格初始化鸭子数据（性别：0母，1公）
     */
    private static void initDuckList(ArrayList<Duck> list) {
        list.add(new Duck("唐老鸭", 1, 10.2));
        list.add(new Duck("王小鸭", 0, 5.8));
        list.add(new Duck("张老鸭", 0, 8.5));
        list.add(new Duck("唐小鸭", 0, 6.6));
        list.add(new Duck("李老鸭", 1, 7.8));
        list.add(new Duck("李小鸭", 1, 3.3));
        list.add(new Duck("张小鸭", 0, 2.5));
        list.add(new Duck("唐大鸭", 1, 6.8));
        list.add(new Duck("李大鸭", 1, 4.7));
        list.add(new Duck("张老鸭", 0, 7.7));
        list.add(new Duck("王大鸭", 1, 6.2));
    }

    public static void main(String[] args) {
        // ===================== 第1题：ArrayList操作 =====================

        // 使用多种方法遍历该List
        duckService.traverseList(list);

        // 判断该List中是公鸭多还是母鸭多
        duckService.compareSexCount(list);

        // 判断该List中是李姓鸭子多还是王姓鸭子多
        duckService.compareSurnameCount(list);

        // 计算所有鸭子的平均体重
        BigDecimal avgWeight = duckService.getAvgWeight(list);
        System.out.println("===============所有鸭子的平均体重===============》start");
        System.out.println(avgWeight);
        System.out.println("===============所有鸭子的平均体重===============》end");

        // 删除List中所有的老鸭
        duckService.removeOldDucks(list);

        // ===================== 第2题：HashMap操作 =====================

        // 重新初始化第1题的完整鸭子数据，再放入HashMap
        ArrayList<Duck> listForMap = new ArrayList<>();
        initDuckList(listForMap);
        HashMap<String, Duck> map = duckService.listToMap(listForMap);

        // 使用多种方法遍历该HashMap的键和值
        duckService.traverseMap(map);

        // 判断HashMap中是公鸭多还是母鸭多
        duckService.compareSexCountInMap(map);

        // 判断HashMap中是李姓鸭子多还是王姓鸭子多
        duckService.compareSurnameCountInMap(map);

        // 删除该HashMap中所有的老鸭
        duckService.removeOldDucksInMap(map);
    }
}
