package ShapeDemo;

public abstract class Shape {
    double area;
    double per;
    String color;

    public Shape() {
    }

    public Shape(String color) {
        this.color = color;
    }

    public abstract double getArea();

    public abstract double getPer();

    public abstract void showAll();

    public String getColor() {
        return color;
    }
}
