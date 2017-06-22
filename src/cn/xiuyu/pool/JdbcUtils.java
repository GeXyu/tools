package cn.xiuyu.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 
 * @author gexyuzz
 * @version date��2017��6��22�� ����9:53:35
 */
public class JdbcUtils {
	static{
		try {
			Class.forName(PropertiesUtils.jdbcDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(PropertiesUtils.url, PropertiesUtils.userName, PropertiesUtils.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
