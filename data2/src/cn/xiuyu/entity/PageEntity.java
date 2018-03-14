package cn.xiuyu.entity;

import java.util.List;

public class PageEntity<T> {
	private int count;
	private int size = 20;
	private int start;
	private int end;
	private int pageNum;
	private int nowpage;
	private List<T> entity;
	
	public void Calculation(){
		//设置页数
		if(this.count % size==0){
			this.pageNum = count/size;
		}else{
			this.pageNum = count/size + 1;
		}
		this.start = start * size;
		this.end = end * size;
		//设置 当前页
		this.nowpage = start/size;;	
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getNowpage() {
		return nowpage;
	}
	public void setNowpage(int nowpage) {
		this.nowpage = nowpage;
	}
	
	public PageEntity(int count, int size, int start, int end, int pageNum, int nowpage, List<T> entity) {
		super();
		this.count = count;
		this.size = size;
		this.start = start;
		this.end = end;
		this.pageNum = pageNum;
		this.nowpage = nowpage;
		this.entity = entity;
	}
	public List<T> getEntity() {
		return entity;
	}
	public void setEntity(List<T> entity) {
		this.entity = entity;
	}
	public PageEntity() {
		super();
	}
	public PageEntity(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}
	
}
