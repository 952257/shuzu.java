import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_Demo02 {
    /**
     *SQL注入
     */
    public static void main(String[] args) throws Exception {

        //1.注册驱动
        //Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取数据库连接对象
        String url = "jdbc:mysql://127.0.0.1:3306/school_project_db";
        String username = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println(connection);
        // 3.获取执行SQL语句的对象
        Statement statement = connection.createStatement();
        //4.编写SQL语句，并执行，接受返回的结果集

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String loginUserName = scanner.nextLine();

        String sql = "SELECT count(*) count FROM user WHERE user_name = '" + loginUserName +"'";
        ResultSet resultSet = statement.executeQuery(sql);

        //5.处理结果：遍历resultSet结果集
        while (resultSet.next()){
            int count = resultSet.getInt("count");
            if(count > 0){
                System.out.println("登录成功");
            }else{
                System.out.println("登录失败");
            }
        }

        //6.释放资源（先开后关原则）
        resultSet.close();
        statement.close();
        connection.close();


    }
}
