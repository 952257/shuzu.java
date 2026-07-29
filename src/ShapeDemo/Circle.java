package ShapeDemo;

public class Circle extends Shape {
    double radius;

    public Circle(double radius, String color) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        area = Math.PI * radius * radius;
        return area;
    }

    @Override
    public double getPer() {
        per = 2 * Math.PI * radius;
        return per;
    }

    @Override
    public void showAll() {
        System.out.println("圆的颜色是：" + getColor());
        System.out.println("圆的半径是：" + radius);
        System.out.println("圆的面积是：" + getArea());
        System.out.println("圆的周长是：" + getPer());
        System.out.println();
    }
}
