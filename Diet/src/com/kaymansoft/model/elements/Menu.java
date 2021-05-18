package com.kaymansoft.model.elements;

import java.io.Serializable;

public class Menu extends ModelElement implements Serializable {

	private static final long serialVersionUID = 1L;
	private long imageId = -1;

	public Menu() {}
	
	public Menu(long id, String name, String description, boolean isUserDefined) {
		super(id,name,description,isUserDefined);
	}
	
	public long getImageId() {
		return imageId;
	}
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	
}
