package com.kaymansoft.model.elements;

import java.io.Serializable;

public class Food extends ModelElement implements Serializable {

	private static final long serialVersionUID = 1L;
	String unit = null;
	double calories = Double.NaN, fat = Double.NaN, protein = Double.NaN, carbohydrates = Double.NaN, quantity = 1.0;
	private long imageId = -1;
	
	public Food(){}
		
	public Food(long id, String name, String unit, double calories, boolean isUserDefined) {
		super(id, name, null, isUserDefined);
		this.setUnit(unit);
		this.setCalories(calories); 
	}
	
	public Food(long id, String name, String description, String unit,
			boolean isUserDefined, double calories, double fat, double protein,
			double carbohydrates) {
		
		super(id, name, description, isUserDefined);
		
		this.unit = unit;
		this.calories = calories;
		this.fat = fat;
		this.protein = protein;
		this.carbohydrates = carbohydrates;
	}
	
	public double getFat() {
		return fat;
	}
	
	public void setFat(double fat) {
		this.fat = fat;
	}
	public double getProtein() {
		return protein;
	}
	public void setProtein(double protein) {
		this.protein = protein;
	}
	public double getCarbohydrates() {
		return carbohydrates;
	}
	public void setCarbohydrates(double carbohydrates) {
		this.carbohydrates = carbohydrates;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public boolean isUserDefined() {
		return isUserDefined;
	}
	public void setUserDefined(boolean isUserDefined) {
		this.isUserDefined = isUserDefined;
	}
	public double getCalories() {
		return calories;
	}
	public void setCalories(double calories) {
		this.calories = calories;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}		
	
}
