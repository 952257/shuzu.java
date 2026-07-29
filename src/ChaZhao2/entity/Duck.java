package ChaZhao2.entity;

/**
 * 鸭子实体类
 */
public class Duck {
    private String name;
    private int sex; // 0：母 1：公
    private double weight;

    public Duck(String name, int sex, double weight) {
        this.name = name;
        this.sex = sex;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Duck{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", weight=" + weight +
                '}';
    }
}
