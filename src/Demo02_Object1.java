import java.util.Scanner;

public class Demo02_Object1 {
    /**
     * 面向对象介绍
     *  1.面向过程：
     *      自己的事情自己干
     *      比如洗衣服：自己亲历亲为，找个盆-》加入水-》加入洗衣粉-》用手去搓
     *  2.面向对象：
     *      自己的事情让别人去帮忙干
     *      比如洗衣服：自己的事情让别人干，全自动洗衣机
     *  3.为什么要用面向对象思想编程：懒、图省事
     *      有些功能别人都实现好了，我们直接使用就可以了，简化了我们的代码编写的过程，减少了我们的代码量
     *  4.什么时候使用面向对象思想编程
     *      当我们在类中需要使用别人的功能时，我们就可以直接调用别人的功能，至于别人这个是如何实现的
     *      我们并不关心，我们需要的只是拿到结果
     *  5.如何使用对象
     *      new 这个类得到这个类的对象
     */

    /**
     *面向对象的“类”和“对象”
     *  类：泛指一类事物的抽象表现形式
     *      比如：人（比较宽泛，没有指定具体的一个人）、手机
     *  如何定义一个类，类的组成：
     *      属性（成员变量）：一类事物的特征
     *      功能（成员方法）：一类事物能做什么
     *  对象：一类事物的具体表现形式
     *    类名 对象名 = new 类名();
     *
     *   调用成员：
     *      对象名.成员变量
     *      对象名.成员方法();
     *
     *
     */

    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        // alt + 回车：自动修正、导包
        //String next = sc.next();

        // 通过new创建一个类的对象
        Person person = new Person();
        person.name = "蔡徐坤";
        person.age = 18;

        System.out.println(person.name);
        System.out.println(person.age);
        person.sing("只因你太美，喔 bay");

        Phone phone = new Phone();
        phone.brand = "华为";
        phone.price = "5999.99";
        phone.color = "黑色";
        phone.call();


    }

}
