package com.kaymansoft.proximity.client.cache;

import java.util.ArrayList;

import com.kaymansoft.proximity.client.data.CacheHeatResponse;
import com.kaymansoft.proximity.client.data.HeatQueryData;
import com.kaymansoft.proximity.model.HeatResponse;

import android.graphics.drawable.Drawable;

public class CacheProviderStub implements CacheProvider{
	public Drawable getImage(String image) throws RuntimeException{
		return null;
	}
	
	public CacheHeatResponse getWhot(int[] catId, double maxLat, double maxLon, double minLat, double minLon) throws RuntimeException{
		CacheHeatResponse chr = new CacheHeatResponse();
		chr.responses = new ArrayList<HeatResponse>();
		chr.queries = new ArrayList<HeatQueryData>();
		HeatQueryData hq = new HeatQueryData();
		hq.after = Long.MIN_VALUE;
		hq.catId = catId;
		hq.minLat = minLat;
		hq.minLon = minLon;
		hq.maxLat = maxLat;
		hq.maxLon = maxLon;
		chr.queries.add(hq);
		return chr;
	}

	public void store(Drawable drw, String image)throws RuntimeException{
		
	}

	public void store(HeatResponse hr, HeatQueryData hq)throws RuntimeException{
		
	}
	
	public void gc() throws RuntimeException{
		
	}
}
