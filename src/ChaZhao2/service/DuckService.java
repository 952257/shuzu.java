package ChaZhao2.service;

import ChaZhao2.entity.Duck;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface DuckService {

    // 使用多种方法遍历该List
    void traverseList(List<Duck> list);

    // 判断该List中是公鸭多还是母鸭多
    void compareSexCount(List<Duck> list);

    // 判断该List中是李姓鸭子多还是王姓鸭子多
    void compareSurnameCount(List<Duck> list);

    // 计算所有鸭子的平均体重
    BigDecimal getAvgWeight(List<Duck> list);

    // 删除List中所有的老鸭
    void removeOldDucks(List<Duck> list);

    // 将List中的所有鸭子放入HashMap，名字做key，鸭子对象做value
    HashMap<String, Duck> listToMap(List<Duck> list);

    // 使用多种方法遍历HashMap的键和值
    void traverseMap(HashMap<String, Duck> map);

    // 判断HashMap中是公鸭多还是母鸭多
    void compareSexCountInMap(HashMap<String, Duck> map);

    // 判断HashMap中是李姓鸭子多还是王姓鸭子多
    void compareSurnameCountInMap(HashMap<String, Duck> map);

    // 删除HashMap中所有的老鸭
    void removeOldDucksInMap(HashMap<String, Duck> map);
}
