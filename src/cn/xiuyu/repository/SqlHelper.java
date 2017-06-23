package cn.xiuyu.repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author gexyuzz
 * @version date：2017年6月22日 下午5:21:40 
 * @param <T>
 */
public class SqlHelper<T> {
	private String entityName;
	private Class clazz;
	private List<String> beanList;
	public SqlHelper(String entityName,Class clazz){
		this.entityName = entityName;
		this.clazz = clazz;
	}
	//把实体封装成map
	public static <T> Map<String,Object> getfield(T t){
			try {
				Map<String,Object> maps = new HashMap<String,Object>();
				Field[] fields = t.getClass().getDeclaredFields();
				if(fields.length <=0){
					return null;
				}
				for(Field f:fields){
					f.setAccessible(true);
					String name = f.getName();
					Object values;
					values = f.get(t);
					maps.put(name, values);
					
				}
				return maps;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}
	
	//字段转换成list
	public static <T> List<String> getfieldTolist(Class clazz){
		List<String> list = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length <=0){
			return null;
		}
		for(Field f:fields){
			f.setAccessible(true);
			String name = f.getName();	
			list.add(name);
		}	
		return list;
	}
	
	//构造插入语句
	public String insertSql(T t){
		Map<String,Object> bean = SqlHelper.getfield(t);
		String prefix = "insert into " + entityName + "(";
		String suffix = " values(";
		int size = bean.size();
		int i = 0;
		for(Entry<String, Object> maps: bean.entrySet()){
			String key = maps.getKey();
			String value = (String)maps.getValue();
			if(i == size-1){
				//说明是最后一个字段
				prefix = prefix + key + ")";
				suffix = suffix + " '"+value + "');";
			}else{
				prefix = prefix + key + ", ";
				suffix = suffix + " '" + value + "',";
			}
			i++;
		}
		String sql = prefix + suffix;
		
		return sql;
	}
	//构建查询语句
	public String QuerySql(int id){
		beanList = getfieldTolist(clazz);
		String prefix = "select ";
		String suffix = " from " + entityName + "  where id=" + id;
		int size = beanList.size();
		for(int i=0; i<size; i++){
			if(i == (size-1)){
				//说明是最后一一个
				prefix = prefix +  beanList.get(i) + " ";
			}else{
				prefix = prefix + beanList.get(i) + ", ";
			}
		}
		
		String sql = prefix + suffix + ";";
		return sql;
	}
	
	public String DeleteSql(int id){
		return "delete from "+ entityName  + " where id = " + id+" ;";
	}
	//构建更新语句
	public String UpdateSql(T t){
		String prefix = "update " + entityName ;
		String suffix = " where id=";
		
		Map<String,Object> bean = SqlHelper.getfield(t);
		int size = bean.size();
		int i = 0;
		for(Entry<String, Object> maps: bean.entrySet()){
			String key = maps.getKey();
			String value = (String)maps.getValue();
			if(i == size-1){//说明是最后一个字段
				prefix = prefix +"  " + key + "='" +value +"' "; 
				i++;
				continue;
			}
			//如果是id字段
			if(key.equals("id")){
				suffix = suffix + "'"+value+"'";
				i++;
				continue;
			}
			//说明是第一个字段
			if(i == 1){
				prefix = prefix +" set " + key + "= '" +value + "',"; 
				i++;
				continue;
			}
			prefix = prefix +" " + key + "= '" +value + "',"; 
			i++;
				
		}
		String sql = prefix + suffix + ";";
		return sql;
	}
	
	//把值设置到实体
	public T setEntity(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException{
		Object ojb =  clazz.newInstance();
		Field[] fields = ojb.getClass().getDeclaredFields();
		
		while(rs.next()){
			for(Field f:fields){
				f.setAccessible(true);
				String name = f.getName();
				String value = rs.getString(name);
				f.set(ojb, value);
			}
		}
		return (T) ojb;
	}
	
	//把值设置到实体
		public List<T> setList(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException{
			List<T> lists = new ArrayList<T>();
			Field[] fields = clazz.getDeclaredFields();
			
			while(rs.next()){
				Object ojb =  clazz.newInstance();
				for(Field f:fields){
					f.setAccessible(true);
					String name = f.getName();
					String value = rs.getString(name);
					f.set(ojb, value);	
				}
				lists.add((T)ojb);
			}
			return lists;
		}
	
	//构造count语句
	public String CountSql(){
		return "select count(1) from " + entityName + " ; ";
	}
	
	//构造查询前缀
	public String makeprefix(){
		beanList = getfieldTolist(clazz);
		String prefix = "select ";
		int size = beanList.size();
		for(int i=0; i<size; i++){
			if(i == (size-1)){
				//说明是最后一一个
				prefix = prefix +  beanList.get(i) + " ";
			}else{
				prefix = prefix + beanList.get(i) + ", ";
			}
		}
		return prefix;
	}
}
