package com.kaymansoft.model.elements;

import java.io.Serializable;


public class Category extends ModelElement implements Serializable {

	private static final long serialVersionUID = 1L;
	private long imageId = -1;
	
	public Category() {}
	
	public Category(long id, String name, String description, 
			boolean isUserDefined) {
	 super(id,name,description,isUserDefined);
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUserDefined() {
		return isUserDefined;
	}
	public void setUserDefined(boolean isUserDefined) {
		this.isUserDefined = isUserDefined;
	}
	public long getImageId() {
		return imageId;
	}
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

}
