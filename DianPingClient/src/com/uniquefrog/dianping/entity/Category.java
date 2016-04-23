package com.uniquefrog.dianping.entity;

public class Category {
	private String categoryId;
	private long categeryNumber;
	@Override
	public String toString() {
		return categoryId+"-->"+categeryNumber;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public long getCategeryNumber() {
		return categeryNumber;
	}
	public void setCategeryNumber(long categeryNumber) {
		this.categeryNumber = categeryNumber;
	}
	
}
