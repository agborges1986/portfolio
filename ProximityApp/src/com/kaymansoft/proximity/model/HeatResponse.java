package com.kaymansoft.proximity.model;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author
 *
 * A class to represent the returned data of heat queries. 
 */
public class HeatResponse {
	public PlaceDesc[] places;
	
	public static HeatResponse fromJSON(JSONObject json) throws RuntimeException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, JSONException {
		HeatResponse ret = ModelUtils.getObjectFromJSON(json, HeatResponse.class);
		JSONArray arr = json.optJSONArray("places");
		if(arr != null){
			ret.places = new PlaceDesc[arr.length()];
			for(int i = 0; i < arr.length(); i++){
				ret.places[i] = PlaceDesc.fromJSON(arr.getJSONObject(i));
			}
		}else {
			JSONObject njson = json.optJSONObject("places");
			if(njson != null){
				ret.places = new PlaceDesc[]{PlaceDesc.fromJSON(njson)};
			}else{
				ret.places = new PlaceDesc[0];
			}
		}
		return ret;
	}
}
