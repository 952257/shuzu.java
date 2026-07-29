package zuoye2;

public class Person {

    private int age;

    public Person() {
    }

    public Person(int age) {
        setAge(age);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0 && age <= 120) {
            this.age = age;
        } else {
            this.age = 0;
        }
    }

    public void show() {
        System.out.println("年龄信息：" + age + "岁");
    }
}
