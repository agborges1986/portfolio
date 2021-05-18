package com.kaymansoft.proximity.client.data;

import java.io.InputStream;

public class PostPlaceData {
	
	public PostPlaceData(
			String name,
			String description,
			int heatDuration,
			InputStream image,
			double latitude,
			double longitude,
			int rating,
			int categoryId,
			String sessionId) {
		
		super();
		
		this.name = name;
		this.description = description;
		this.heatDuration = heatDuration;
		this.image = image;
		this.latitude = latitude;
		this.longitude = longitude;
		this.rating = rating;
		this.categoryId = categoryId;
		this.sessionId = sessionId;
	}
	
	public String name;
	
	public String description;
	
	public int heatDuration;
	
	public InputStream image;
	
	public double latitude;
	
	public double longitude;
	
	public int rating;
	
	public int categoryId;
	
	public String sessionId;
}