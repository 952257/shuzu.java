package zuoye2;

public class PersonTest {
    public static void main(String[] args) {
        Person p1 = new Person();
        p1.setAge(25);
        p1.show();

        Person p2 = new Person(30);
        p2.show();

        Person p3 = new Person();
        p3.setAge(200);
        p3.show();

        Person p4 = new Person(-5);
        p4.show();
    }
}
