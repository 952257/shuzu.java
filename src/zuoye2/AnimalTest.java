package zuoye2;

public class AnimalTest {
    public static void main(String[] args) {
        Animal animal = new Dog("旺财");
        animal.eat();

        Animal animal2 = new Animal("普通动物");
        animal2.eat();
    }
}
