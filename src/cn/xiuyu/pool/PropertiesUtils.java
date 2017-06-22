package cn.xiuyu.pool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author gexyuzz
 * @version date：2017年6月22日 下午9:53:40
 */
public class PropertiesUtils {
	public static String jdbcDriver;
	public static String userName;
	public static String password;
	public static String url;
	public static String MAXPOOLSIZE;
	public static String InitPOOLSIZE;
	static{
		try {
			InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties p = new Properties();
			p.load(in);
			jdbcDriver = p.getProperty("jdbcDriver");
			userName = p.getProperty("userName");
			password = p.getProperty("password");
			url = p.getProperty("url");
			MAXPOOLSIZE = p.getProperty("MAXPOOLSIZE");
			InitPOOLSIZE = p.getProperty("InitPOOLSIZE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
