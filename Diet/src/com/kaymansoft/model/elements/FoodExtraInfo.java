package com.kaymansoft.model.elements;

import java.io.Serializable;

public class FoodExtraInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean isBreakfast = false, isSnack = false, isLunch = false, isDinner = false;
	private int priority = 0;
	private long id = -1;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	public boolean isBreakfast() {
		return isBreakfast;
	}
	public void setBreakfast(boolean isBreakfast) {
		this.isBreakfast = isBreakfast;
	}
	public boolean isSnack() {
		return isSnack;
	}
	public void setSnack(boolean isSnack) {
		this.isSnack = isSnack;
	}
	public boolean isLunch() {
		return isLunch;
	}
	public void setLunch(boolean isLunch) {
		this.isLunch = isLunch;
	}
	public boolean isDinner() {
		return isDinner;
	}
	public void setDinner(boolean isDinner) {
		this.isDinner = isDinner;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		if(priority >=1 && priority <= 10)
			this.priority = priority;
		else
			this.priority = 4;//una prioridad media
	}
}
