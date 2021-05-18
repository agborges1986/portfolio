package com.kaymansoft.model.elements;

import java.io.Serializable;

public class Weight implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private double weight;
	private String date;
	
	public Weight(long id, double weight, String date) {
		this.id = id;
		this.weight = weight;
		this.date = date;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getWeightValue() {
		return weight;
	}
	public void setWeightValue(double weight) {
		this.weight = weight;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
