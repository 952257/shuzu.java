package zuoye2;

public class Dog extends Animal {

    public Dog() {
    }

    public Dog(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println("小狗在啃骨头");
    }
}
