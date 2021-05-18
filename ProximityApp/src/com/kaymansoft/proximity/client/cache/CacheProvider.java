package com.kaymansoft.proximity.client.cache;

import android.graphics.drawable.Drawable;

import com.kaymansoft.proximity.client.data.CacheHeatResponse;
import com.kaymansoft.proximity.client.data.HeatQueryData;
import com.kaymansoft.proximity.model.HeatResponse;

public interface CacheProvider {

	public Drawable getImage(String image) throws RuntimeException;

	public CacheHeatResponse getWhot(int[] catId, double maxLat, double maxLon,
			double minLat, double minLon) throws RuntimeException;

	public void store(Drawable drw, String image) throws RuntimeException;

	public void store(HeatResponse hr, HeatQueryData hq) throws RuntimeException;

	public void gc() throws RuntimeException;

}