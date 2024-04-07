//此类的功能是连接数据库
import java.sql.Connection;
import java.sql.DriverManager;

//连接数据库
public class DBConnection {
	//DBName:数据库名 ，id : 用户名（root）password:数据库密码
	public static Connection ConnectDB(String DBName,String id,String password) {
		Connection con = null;
		String uri="jdbc:mysql://localhost:3306/"+DBName;
		try {
			Class.forName("com.mysql.jdbc.Driver");//加载驱动
		} catch (Exception e) {}
				
		try {
			con = DriverManager.getConnection(uri,id,password);//连接
		} catch (Exception e) {e.printStackTrace();}
		
		return con;
	}
}
