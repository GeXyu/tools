package cn.xiuyu.pool;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
/**
 * 数据库连接池简单实现
 * @author gexyuzz
 * @version date：2017年6月22日 下午9:53:29
 */
public class DataSourcePool implements DataSource{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataSourcePool.class);
	private static final LinkedList<Connection> pool = new LinkedList<Connection>();
	private int useconn = 0; //已经使用的连接
	int maxpoolsize;
	int initPoolSize;
	public  DataSourcePool(){
		maxpoolsize = Integer.parseInt(PropertiesUtils.MAXPOOLSIZE);
		initPoolSize = Integer.parseInt(PropertiesUtils.InitPOOLSIZE);
		for(int i=0; i<initPoolSize; i++){
			pool.add(JdbcUtils.getConnection());
		}
		logger.info("pool init success ....");
		
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized Connection getConnection() throws SQLException {
		//说明获取不到连接需要扩容
		if(pool.size() == 0 && useconn <maxpoolsize){
			if(maxpoolsize % 10 ==0){
				for(int i=0; i<10; i++){
					pool.add(JdbcUtils.getConnection());
					logger.debug("Expansion success ....");
				}
			}else{
				for(int i=0; i<maxpoolsize - 10; i++){
					pool.add(JdbcUtils.getConnection());
					logger.debug("Expansion success ....");
				}
			}
			
		}
		
		if(pool.size()>0){
			final Connection conn = pool.removeFirst();
			useconn ++;
			return (Connection)Proxy.newProxyInstance(conn.getClass().getClassLoader(),
					conn.getClass().getInterfaces(),
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if(method.getName().equals("close")){
								pool.add(conn);
								logger.info("connection back to pool success ....");
							}else{
								Object ojb = method.invoke(conn, args);
								return ojb;
							}
							return null;
						}
					});
		}
		
		logger.debug("not enough connections available");
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
