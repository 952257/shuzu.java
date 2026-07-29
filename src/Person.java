public class Person {
    String name;
    String sex;
    int age;
    double score;

    // 根据之前学习的方法，把static关键字去掉即可
    public void sing(String song){
        // i是局部变量
        int i = 100;
        System.out.println(i);
        //System.out.println(name + "在唱歌：" + song);
    }
    public void eat(){
        System.out.println(name);
    }
}
