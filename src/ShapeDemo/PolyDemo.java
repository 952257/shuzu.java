package ShapeDemo;

public class PolyDemo {
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(10, 5, "蓝色");
        Circle circle = new Circle(3, "红色");

        rectangle.showAll();
        circle.showAll();
    }
}
