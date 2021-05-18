package com.kaymansoft.proximity.client.network;

import java.io.InputStream;

import com.kaymansoft.proximity.model.HeatResponse;

public interface PlaceProvider {
	// TODO Improve this
	public int postPlace(String name, String description, int heatDuration,
			InputStream image, double latitude, double longitude, int rating,
			int categoryId, String sessionId) throws RuntimeException;

	// TODO Improve this
	public boolean commentPlace(String text, int placeId, int rating,
			InputStream image, String sessionId, int categoryId, int heatDuration)
			throws RuntimeException;

	// TODO Improve this
	public HeatResponse whatIsHot(long after, int[] catId, double maxLat, double maxLon, double minLat, double minLon) throws RuntimeException;
}
