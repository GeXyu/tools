package cn.xiuyu.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xiuyu.annotation.Delete;
import cn.xiuyu.annotation.Query;
import cn.xiuyu.annotation.Updata;
import cn.xiuyu.response.CurdResponse;
import cn.xiuyu.response.Response;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ResponseFactory implements MethodInterceptor{
	private static final ResponseFactory instance = new ResponseFactory();
	private static final Logger log = LoggerFactory.getLogger(ResponseFactory.class);
	public static ResponseFactory getInstance(){
		return instance;
	}
	private static boolean status = true;
	CurdResponse response;
	Class clazz;
	Map<String,String> maps = new HashMap<String,String>();
	private Class entityClazz;
	
	public <T> T createResponse(Class clazz,Class entityClazz){
		this.clazz = clazz;
		this.entityClazz = entityClazz;
		if(Response.class.isAssignableFrom(clazz)){
			if(status){
				//执行初始化方法
				initMethod(clazz,entityClazz);
				status = false;
			}
			Enhancer en = new Enhancer();
			en.setCallback(this);
			en.setSuperclass(clazz);
			return (T) en.create();	
			
		}
		System.out.println("该类没有实现response 接口");
		return null;
	}

	private void initMethod(Class clazz,Class entityClazz) {
		response = new CurdResponse(entityClazz);
		Method[] methods = clazz.getMethods();
		//获取所有带注解的method
		for(Method m:methods){
			Annotation[] annotations = m.getAnnotations();
			//如果没有注解就返回
			if(annotations.length == 0){
				return;
			}
			for(Annotation annot:annotations){
				Class<? extends Annotation> type = annot.annotationType();
				if(type.equals(Query.class)){
					QueryHandler(m);
				}else if(type.equals(Updata.class)){
					UpdataHandler(m);
				}else if(type.equals(Delete.class)){
					DeleteHandler(m);
				}
			}
		}
		
	}
	private void DeleteHandler(Method m) {
		Delete annotation = m.getAnnotation(Delete.class);
		String sql = annotation.value();
		maps.put(m.getName(), sql);
		
	}
	private void UpdataHandler(Method m) {
		Updata annotation = m.getAnnotation(Updata.class);
		String sql = annotation.value();
		maps.put(m.getName(), sql);
	}
	private void QueryHandler(Method m) {
		Query annotation = m.getAnnotation(Query.class);
		String sql = annotation.value();
		maps.put(m.getName(), sql);
	}
	
	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy arg3) throws Throwable {
		String name = method.getName();
		//说明是带注解的方法
		if(maps.containsKey(name)){
			String sql = (maps.get(name)).toLowerCase();
			for(int i=0; i<args.length; i++){
				if(sql.indexOf("?") <0){
					continue;
				}
				sql = sql.replaceFirst("\\?", args[i]+" ");
			}
			
			log.info("sql:--" +sql );
			if(sql.startsWith("from")){
				Object executeQuerySql = response.executeQuerySql(sql);
				return executeQuerySql;
			}else{
				
				response.executeUpdateSql(sql);
			}
		}else{

			try{
				Object invoke = method.invoke(response, args);
				return invoke;
			}catch(IllegalArgumentException e){
				System.out.println("并没有添加注解 ...." + e);
			}
			
		}
		return null;
	}
}

