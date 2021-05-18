package com.kaymansoft.proximity.model;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author
 *
 * A class to represent a hot place.
 */
public class PlaceDesc {

	public int cantRating;

	public int[] categories;

	public CommentDesc[] comments;

	public long creationTime;

	public long heatOverTime;

	public double latitude;

	public double longitude;

	public String name;

	public int totalRating;

	public int id;
	
	public PlaceDesc() {
	}

	public static PlaceDesc fromJSON(JSONObject json) throws RuntimeException,
	NoSuchMethodException, InstantiationException,
	IllegalAccessException, InvocationTargetException, JSONException{
		PlaceDesc ret = ModelUtils.getObjectFromJSON(json, PlaceDesc.class);
		JSONArray arr = json.optJSONArray("comments");
		if(arr != null){
			ret.comments = new CommentDesc[arr.length()];
			for(int i = 0; i < arr.length(); i++){
				ret.comments[i] = CommentDesc.fromJSON(arr.getJSONObject(i));
			}
		}else {
			JSONObject njson = json.optJSONObject("comments");
			if(njson != null){
				ret.comments = new CommentDesc[]{CommentDesc.fromJSON(njson)};
			}else{
				ret.comments = new CommentDesc[0];
			}
		}
		return ret;
	}
}
