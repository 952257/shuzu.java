package ChaZhao2.service.impl;

import ChaZhao2.entity.Duck;
import ChaZhao2.service.DuckService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DuckServiceImpl implements DuckService {

    @Override
    public void traverseList(List<Duck> list) {
        if (list.isEmpty()) {
            return;
        }

        // 方式1：普通for循环（下标遍历）
        System.out.println("===============方式1：普通for循环遍历List===============》start");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("===============方式1：普通for循环遍历List===============》end");

        // 方式2：增强for循环（foreach）
        System.out.println("===============方式2：增强for循环遍历List===============》start");
        for (Duck duck : list) {
            System.out.println(duck);
        }
        System.out.println("===============方式2：增强for循环遍历List===============》end");

        // 方式3：Iterator迭代器遍历
        System.out.println("===============方式3：Iterator迭代器遍历List===============》start");
        Iterator<Duck> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("===============方式3：Iterator迭代器遍历List===============》end");
    }

    @Override
    public void compareSexCount(List<Duck> list) {
        int maleCount = 0;
        int femaleCount = 0;
        if (!list.isEmpty()) {
            for (Duck duck : list) {
                if (duck.getSex() == 1) {
                    maleCount++;
                } else if (duck.getSex() == 0) {
                    femaleCount++;
                }
            }
        }
        System.out.println("===============List中判断公鸭与母鸭数量===============》start");
        System.out.println("公鸭数量：" + maleCount + "，母鸭数量：" + femaleCount);
        if (maleCount > femaleCount) {
            System.out.println("公鸭多");
        } else if (femaleCount > maleCount) {
            System.out.println("母鸭多");
        } else {
            System.out.println("公鸭与母鸭数量相等");
        }
        System.out.println("===============List中判断公鸭与母鸭数量===============》end");
    }

    @Override
    public void compareSurnameCount(List<Duck> list) {
        int liCount = 0;
        int wangCount = 0;
        if (!list.isEmpty()) {
            for (Duck duck : list) {
                String name = duck.getName();
                if (name.startsWith("李")) {
                    liCount++;
                } else if (name.startsWith("王")) {
                    wangCount++;
                }
            }
        }
        System.out.println("===============List中判断李姓与王姓鸭子数量===============》start");
        System.out.println("李姓鸭子数量：" + liCount + "，王姓鸭子数量：" + wangCount);
        if (liCount > wangCount) {
            System.out.println("李姓鸭子多");
        } else if (wangCount > liCount) {
            System.out.println("王姓鸭子多");
        } else {
            System.out.println("李姓与王姓鸭子数量相等");
        }
        System.out.println("===============List中判断李姓与王姓鸭子数量===============》end");
    }

    @Override
    public BigDecimal getAvgWeight(List<Duck> list) {
        BigDecimal weightSum = new BigDecimal("0");
        if (!list.isEmpty()) {
            for (Duck duck : list) {
                weightSum = weightSum.add(new BigDecimal(String.valueOf(duck.getWeight())));
            }
            return weightSum.divide(new BigDecimal(String.valueOf(list.size())), 1, RoundingMode.HALF_UP);
        }
        return null;
    }

    @Override
    public void removeOldDucks(List<Duck> list) {
        if (!list.isEmpty()) {
            Iterator<Duck> iterator = list.iterator();
            while (iterator.hasNext()) {
                Duck duck = iterator.next();
                // 名字中包含"老鸭"的即为老鸭
                if (duck.getName().contains("老鸭")) {
                    iterator.remove();
                }
            }
        }
        System.out.println("===============删除所有老鸭后的List===============》start");
        System.out.println(list);
        System.out.println("===============删除所有老鸭后的List===============》end");
    }

    @Override
    public HashMap<String, Duck> listToMap(List<Duck> list) {
        HashMap<String, Duck> map = new HashMap<>();
        if (!list.isEmpty()) {
            for (Duck duck : list) {
                // 用鸭子名字做key，鸭子对象做value（同名鸭子后者会覆盖前者）
                map.put(duck.getName(), duck);
            }
        }
        return map;
    }

    @Override
    public void traverseMap(HashMap<String, Duck> map) {
        if (map.isEmpty()) {
            return;
        }

        // 方式1：遍历keySet，再通过key获取value
        System.out.println("===============方式1：keySet遍历HashMap===============》start");
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            System.out.println("key=" + key + ", value=" + map.get(key));
        }
        System.out.println("===============方式1：keySet遍历HashMap===============》end");

        // 方式2：遍历values
        System.out.println("===============方式2：values遍历HashMap===============》start");
        for (Duck duck : map.values()) {
            System.out.println(duck);
        }
        System.out.println("===============方式2：values遍历HashMap===============》end");

        // 方式3：遍历entrySet
        System.out.println("===============方式3：entrySet遍历HashMap===============》start");
        for (Map.Entry<String, Duck> entry : map.entrySet()) {
            System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
        }
        System.out.println("===============方式3：entrySet遍历HashMap===============》end");

        // 方式4：Iterator迭代器遍历entrySet
        System.out.println("===============方式4：Iterator遍历HashMap===============》start");
        Iterator<Map.Entry<String, Duck>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Duck> entry = iterator.next();
            System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
        }
        System.out.println("===============方式4：Iterator遍历HashMap===============》end");
    }

    @Override
    public void compareSexCountInMap(HashMap<String, Duck> map) {
        int maleCount = 0;
        int femaleCount = 0;
        if (!map.isEmpty()) {
            for (Duck duck : map.values()) {
                if (duck.getSex() == 1) {
                    maleCount++;
                } else if (duck.getSex() == 0) {
                    femaleCount++;
                }
            }
        }
        System.out.println("===============HashMap中判断公鸭与母鸭数量===============》start");
        System.out.println("公鸭数量：" + maleCount + "，母鸭数量：" + femaleCount);
        if (maleCount > femaleCount) {
            System.out.println("公鸭多");
        } else if (femaleCount > maleCount) {
            System.out.println("母鸭多");
        } else {
            System.out.println("公鸭与母鸭数量相等");
        }
        System.out.println("===============HashMap中判断公鸭与母鸭数量===============》end");
    }

    @Override
    public void compareSurnameCountInMap(HashMap<String, Duck> map) {
        int liCount = 0;
        int wangCount = 0;
        if (!map.isEmpty()) {
            for (Duck duck : map.values()) {
                String name = duck.getName();
                if (name.startsWith("李")) {
                    liCount++;
                } else if (name.startsWith("王")) {
                    wangCount++;
                }
            }
        }
        System.out.println("===============HashMap中判断李姓与王姓鸭子数量===============》start");
        System.out.println("李姓鸭子数量：" + liCount + "，王姓鸭子数量：" + wangCount);
        if (liCount > wangCount) {
            System.out.println("李姓鸭子多");
        } else if (wangCount > liCount) {
            System.out.println("王姓鸭子多");
        } else {
            System.out.println("李姓与王姓鸭子数量相等");
        }
        System.out.println("===============HashMap中判断李姓与王姓鸭子数量===============》end");
    }

    @Override
    public void removeOldDucksInMap(HashMap<String, Duck> map) {
        if (!map.isEmpty()) {
            Iterator<Map.Entry<String, Duck>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Duck> entry = iterator.next();
                // 名字中包含"老鸭"的即为老鸭
                if (entry.getKey().contains("老鸭")) {
                    iterator.remove();
                }
            }
        }
        System.out.println("===============删除所有老鸭后的HashMap===============》start");
        System.out.println(map);
        System.out.println("===============删除所有老鸭后的HashMap===============》end");
    }
}
