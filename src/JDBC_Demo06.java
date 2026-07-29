import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JDBC_Demo06 {

    /**
     * PreparedStatement 预编译SQL语句
     * 修改数据
     */
    public static void main(String[] args) throws Exception {
        //1.注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_project_db", "root", "root");

        //3.创建PreparedStatement对象，并预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement("update student set stu_age = ? where stu_id = ?");

        //4.为占位符赋值，索引从1开始，编写SQL语句并执行，获取结果
        preparedStatement.setInt(1, 50);
        preparedStatement.setString(2, "15");
        int result = preparedStatement.executeUpdate();

        //5.处理结果
        if(result>0){
            System.out.println("修改成功");
        }else{
            System.out.println("修改失败");
        }

        //6.释放资源(先开后关原则)
        preparedStatement.close();
        connection.close();



    }
}
