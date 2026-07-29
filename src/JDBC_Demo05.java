import java.sql.*;

public class JDBC_Demo05 {

    /**
     * PreparedStatement 预编译SQL语句
     * 新增数据
     */
    public static void main(String[] args) throws Exception {
        //1.注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_project_db","root", "root");

        //3.创建PreparedStatement对象，并预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement("insert into student (stu_name,stu_age,stu_gender, stu_birthday)values  (?, ?, ?, ?)");

        //4.为占位符赋值，索引从1开始，编写SQL语句并执行，获取结果
        preparedStatement.setString(1, "杨幂");
        preparedStatement.setInt(2, 20);
        preparedStatement.setInt(3, 0);
        preparedStatement.setDate(4, new Date(System.currentTimeMillis()));
        int result = preparedStatement.executeUpdate();
        System.out.println("添加结果：" + result);
        //5.处理结果
        if(result>0){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }

        //6.释放资源(先开后关原则)
        preparedStatement.close();
        connection.close();



    }
}
