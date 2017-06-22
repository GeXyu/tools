package cn.xiuyu.response;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xiuyu.main.DataSourcePool;
/**
 * 
 * @author gexyuzz
 * @version date：2017年6月22日 下午6:40:42 
 * @param <T>
 */
public class CurdResponse<T> {
	private static final Logger log = LoggerFactory.getLogger(CurdResponse.class);
	private Class<T> clazz;
	private String entityName;
	private List<String> beanList;
	private SqlHelper<T> sqlHelper;
	private DataSourcePool pool;
	@SuppressWarnings("unchecked")
	public CurdResponse(){
		//反射泛型
		ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
		clazz = (Class<T>) pt.getActualTypeArguments()[0];	
		String classname = clazz.getName();
		entityName = classname.substring(classname.lastIndexOf(".") + 1);
		sqlHelper = new SqlHelper<T>(entityName, clazz);
		pool = new DataSourcePool();
	}
	
	public void insert(T t) throws IllegalArgumentException, IllegalAccessException, SQLException{
		Connection connection = pool.getConnection();
		String sql = sqlHelper.insertSql(t);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
		log.info(sql);
		connection.close();
	}
	
	public T query(int id) throws SQLException, InstantiationException, IllegalAccessException{
		Connection connection = pool.getConnection();
		String sql = sqlHelper.QuerySql(id);
		System.out.println(sql);
		
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery(sql);
		log.info(sql);	
		T setEntity = sqlHelper.setEntity(executeQuery);
		connection.close();
		return  setEntity;
	}
	
	public void delete(int id) throws SQLException{
		Connection connection = pool.getConnection();
		String sql = sqlHelper.DeleteSql(id);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
		log.info(sql);
		connection.close();
	}
	
	public void update(T t) throws SQLException{
		Connection connection = pool.getConnection();
		String sql = sqlHelper.UpdateSql(t);
		System.out.println(sql);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
		log.info(sql);
		connection.close();
		
	}
}
