package jdbc;

import java.util.Date;

/**
 * 实体类
 * 跟表对应
 */
public class User {

    private Integer id;
    private String name;
    private String sex;

    public User(Integer id, String name, String sex, Date birthday, Double height) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
    }

    private java.util.Date birthday;
    private Double height;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", height=" + height +
                '}';
    }
}
