package cn.xiuyu.entity;

public class SortEntity {
	private String orderby;

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public SortEntity(String orderby) {
		super();
		this.orderby = orderby;
	}

	public SortEntity() {
		super();
	}
	
}
