import java.util.ArrayList;
import java.util.Iterator;

public class Collection {
    /**
     * Collection集合（单列集合：一个元素只有一个组成）：顶级集合接口
     * 它就是一个容器，只能存储引用数据类型，长度可变
     *
     *  Collection主要分为两大块：
     *      1.list接口
     *          ArrayList实现类: Array + list
     *              底层是动态数组，它的长度是可变的，通过无参构造创建ArrayList对象，
     *              在第一次add时会创建一个默认长度为10的数组，如果容量不足，则会自动扩容1.5倍
     *          LinkedList实现类: 链表
     *
     *      2.set接口
     */
    public static void main(String[] args) {
        // ArrayList
        // 创建ArrayList集合
        ArrayList list = new ArrayList();
        // 添加元素，追加元素
        list.add(1);
        list.add(2);
        // 添加元素，在指定位置添加元素
        list.add(0, 3);

        // 修改元素，在指定位置修改元素（覆盖原来的元素）
        list.set(0, 0);


        //删除元素
        list.remove(0);
        Integer i = 1;
        list.remove(i);
        System.out.println(list);

        ArrayList list1 = new ArrayList();
        list1.add("a");
        list1.add("b");
        // 删除成功返回true
        boolean bool1 = list1.remove("b");
        // 删除失败返回false
        boolean bool2 = list1.remove("cccc");
        System.out.println(bool1);
        System.out.println(bool2);

        // 获取集合长度
        int size = list1.size();
        System.out.println(size);

        // 集合转成数组
        Object[] array = list1.toArray();

        //list1.remove("a");
        // 判断集合是否为空，为空返回true
        boolean empty = list1.isEmpty();
        System.out.println(empty);
        if(!list1.isEmpty()){
            System.out.println("集合不为空");
        }

        ArrayList list3 = new ArrayList();
        list3.add(0.1);
        list3.add(0.2);
        System.out.println(list3);
        // 删除集合中所有元素
        //list3.clear();
        System.out.println(list3.size());
        //Object i1 = new Integer(1);
        // 判断集合是否包含指定的元素，包含则返回 true 。
        System.out.println(list3.contains(0.3));

        ArrayList list4 = null;
        System.out.println(list4 != null && list4.isEmpty());

        ArrayList list5 = new ArrayList();
        list5.add("a");
        list5.add("b");
        list5.add("b");
        list5.add("c");
        list5.add("d");
        // 查询元素
        //System.out.println(list5.get(0));
        //System.out.println(list5.get(1));
        //System.out.println(list5.get(2));

        System.out.println("================================");
        // 遍历集合:方式一
        for (int j = 0; j < list5.size(); j++) {
            System.out.println(list5.get(j));
        }
        System.out.println("================================");
        // 遍历集合:方式二
        for (Object o : list5) {
            System.out.println(o);
        }
        System.out.println("================================");
        // 遍历集合：方式三 通过迭代器遍历
        Iterator iterator = list5.iterator();
        // hasNext() 判断是否存在下一个元素，存在下一个元素则返回true
        // next() 返回下一个元素
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
}
