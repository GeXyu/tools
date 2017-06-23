package cn.xiuyu.response;

import java.sql.SQLException;

import cn.xiuyu.entity.PageEntity;
import cn.xiuyu.entity.SortEntity;

public interface Response<T> {
	public void insert(T t) throws IllegalArgumentException, IllegalAccessException, SQLException;
	public T query(int id) throws SQLException, InstantiationException, IllegalAccessException;
	public void delete(int id) throws SQLException;
	public void update(T t) throws SQLException;
	public PageEntity<T> QueryBypage(PageEntity<T> entity) throws SQLException, InstantiationException, IllegalAccessException;
	public int Querycount() throws SQLException;
}
