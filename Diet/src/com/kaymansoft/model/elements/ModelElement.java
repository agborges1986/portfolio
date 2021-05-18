package com.kaymansoft.model.elements;

import java.io.Serializable;

public abstract class ModelElement implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected long id = -1;
	protected String name = null;
	protected String description = null;
	protected boolean isUserDefined = false;
	
	public ModelElement(){}
		
	protected ModelElement(long id, String name, String description, boolean isUserDefined) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isUserDefined = isUserDefined;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long menuId) {
		this.id = menuId;
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
		
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null || !getClass().isInstance(o) || getClass().cast(o).getId()<0) 
			return false;
		return getClass().cast(o).getId() == getId();
	}
	
	@Override
	public int hashCode() {
		return (int) getId();
	}
	
}
