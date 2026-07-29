package YuanGong;

public class main
{
    public static void main(String[] args) {
        Employee employee = new Employee("001", "张三", 5000);
        employee.ID();
        employee.work();
        employee.getSalary();
    }
}
