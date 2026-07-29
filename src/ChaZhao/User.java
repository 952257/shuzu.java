package ChaZhao;

public class User {
    private int id;
    private String name;
    private int sex;
    private int age;

    public User(int id, String name, int sex, int age) throws UserException {
        setId(id);
        setName(name);
        setSex(sex);
        setAge(age);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws UserException {
        if (id <= 0) {
            throw new UserException("用户ID必须大于0，当前值：" + id);
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UserException {
        if (name == null || name.trim().isEmpty()) {
            throw new UserException("用户姓名不能为空");
        }
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) throws UserException {
        if (sex != 0 && sex != 1) {
            throw new UserException("性别只能是0(女)或1(男)，当前值：" + sex);
        }
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws UserException {
        if (age < 0) {
            throw new UserException("年龄不能为负数，当前值：" + age);
        }
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', sex=" + sex + ", age=" + age + "}";
    }
}
