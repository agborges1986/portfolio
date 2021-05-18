package com.kaymansoft.model.elements;

import java.io.Serializable;

public class Meal extends ModelElement implements Serializable {

	private static final long serialVersionUID = 1L;
	private long menuId = -1;
	private String date = null;
	
	public Meal() {}
	
	public Meal(long id, String date) {
		super(id,null,null,false);
		this.setDate(date);
	}
	
	public long getMenuId() {
		return menuId;
	}
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
