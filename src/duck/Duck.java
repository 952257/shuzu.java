package duck;

public class Duck {

    private String name;

    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    private double weight;

    @Override
    public String toString() {
        return "Duck{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", weight=" + weight +
                '}';
    }

    public Duck(String name, String sex, double weight) {
        this.name = name;
        this.sex = sex;
        this.weight = weight;
    }
}
