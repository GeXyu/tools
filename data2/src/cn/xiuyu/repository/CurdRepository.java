package cn.xiuyu.repository;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.xiuyu.entity.PageEntity;
import cn.xiuyu.entity.SortEntity;
import cn.xiuyu.pool.DataSourcePool;
;
/**
 * @author gexyuzz
 * @version date：2017年6月22日 下午6:40:42 
 * @param <T>
 */
public class CurdRepository<T> implements Repository<T>{
	private static final Logger log = LoggerFactory.getLogger(CurdRepository.class);
	private Class<T> clazz;
	private String entityName;
	private List<String> beanList;
	private SqlHelper<T> sqlHelper;
	private DataSourcePool pool;
	@SuppressWarnings("unchecked")
	public CurdRepository(){
		//反射泛型
		ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
		clazz = (Class<T>) pt.getActualTypeArguments()[0];	
		String classname = clazz.getName();
		entityName = classname.substring(classname.lastIndexOf(".") + 1);
		sqlHelper = new SqlHelper<T>(entityName, clazz);
		pool = new DataSourcePool();
	}
	public CurdRepository(Class clazz){
		this.clazz = clazz;
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
		
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
		log.info(sql);
		connection.close();	
	}
	
	public void executeUpdateSql(String sql) throws SQLException{
		Connection connection = pool.getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.execute();
		log.info(sql);
		connection.close();
	}
	
	public T executeQuerySql(String sql) throws SQLException, InstantiationException, IllegalAccessException{
		Connection connection = pool.getConnection();
		sql = sqlHelper.makeprefix() + " " + sql;
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery(sql);
		log.info(sql);	
		T setEntity = sqlHelper.setEntity(executeQuery);
		connection.close();
		return setEntity;
	}
	public PageEntity<T> QueryBypage(PageEntity<T> entity) throws SQLException, InstantiationException, IllegalAccessException{
		entity.setCount(Querycount());
		entity.Calculation();//计算数据
		String prefix = sqlHelper.makeprefix();
		String sql = prefix + " from " + entityName + " limit "+ entity.getStart() + " , " + entity.getEnd(); 
		System.out.println(sql);
		
		Connection connection = pool.getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery(sql);
		log.info(sql);	
		entity.setEntity(sqlHelper.setList(executeQuery));
		connection.close();
		return entity;
	}
	//查询	
	public int Querycount() throws SQLException{
		Connection connection = pool.getConnection();
		String countSql = sqlHelper.CountSql();
		PreparedStatement prepareStatement = connection.prepareStatement(countSql);
		ResultSet executeQuery = prepareStatement.executeQuery(countSql);
		while(executeQuery.next()){
			int count = executeQuery.getInt(1);
			return count;
		}
		return 0;
		
	}
}
