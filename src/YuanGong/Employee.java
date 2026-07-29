package YuanGong;

public class Employee {
    String id;
    String name;
    double salary;

    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

   public void ID(){
        System.out.println( "员工" + name + "的ID是：" + id );
    }

    public void work() {
        System.out.println(name + "正在工作。");
    }

    public void getSalary() {
        System.out.println(name + "领取工资：" + salary + "元。");
    }
}
