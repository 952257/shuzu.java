package ShapeDemo;

public class Rectangle extends Shape {
    double width;
    double height;

    public Rectangle() {
    }

    public Rectangle(double width, double height, String color) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        area = width * height;
        return area;
    }

    @Override
    public double getPer() {
        per = (width + height) * 2;
        return per;
    }

    @Override
    public void showAll() {
        System.out.println("矩形的颜色是：" + getColor());
        System.out.println("矩形的长度是：" + width);
        System.out.println("矩形的宽度是：" + height);
        System.out.println("矩形的面积是：" + getArea());
        System.out.println("矩形的周长是：" + getPer());
        System.out.println();
    }
}
