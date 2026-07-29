import java.sql.*;
import java.util.Scanner;

public class JDBC_Demo04 {

    /**
     * PreparedStatement 预编译SQL语句
     * 查询数据
     */
    public static void main(String[] args) throws Exception {
        //1.注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_project_db", "root","root");

        //3.定义SQL语句
        //String sql = "select count(*) as count from student";
        //String sql = "select * from student where stu_id = ?";
        String sql = "select * from student where stu_name = ?";

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String stuName = scanner.nextLine();

        //3.创建PreparedStatement对象，并预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //preparedStatement.setInt(1, 1);
        preparedStatement.setString(1, stuName);

        //4.执行SQL语句，获取结果
        ResultSet resultSet = preparedStatement.executeQuery();

        //5.处理结果
        while (resultSet.next()){
            int stuId = resultSet.getInt("stu_id");
            String stuName1 = resultSet.getString("stu_name");
            System.out.println("学生姓名：" + stuName1 + "，学生ID：" + stuId);
        }

        //6.释放资源(先开后关原则)
        resultSet.close();
        preparedStatement.close();
        connection.close();



    }
}
