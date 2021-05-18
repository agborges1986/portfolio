package com.kaymansoft.proximity.client.data;

import java.io.InputStream;

public class CommentPlaceData {
	
	public CommentPlaceData(
			String text,
			int placeId,
			int rating,
			InputStream image,
			String sessionId,
			int categoryId,
			int heatDuration) {
		
		super();
		
		this.text = text;
		this.placeId = placeId;
		this.rating = rating;
		this.image = image;
		this.sessionId = sessionId;
		this.categoryId = categoryId;
		this.heatDuration = heatDuration;
	}
	
	public String text;
	
	public int placeId;
	
	public int rating;
	
	public InputStream image;
	
	public String sessionId;
	
	public int categoryId;
	
	public int heatDuration;
}