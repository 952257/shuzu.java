package Jdbc;

import java.util.Date;

/**
 * 实体类
 * 跟表 persons 对应
 */
public class Person {

    private Integer id;
    private String name;
    private Double height;
    private Integer sex;
    private Date birthday;

    public Person(Integer id, String name, Double height, Integer sex, Date birthday) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.sex = sex;
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", sex=" + sex +
                ", birthday=" + birthday +
                '}';
    }
}
