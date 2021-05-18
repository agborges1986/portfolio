package com.kaymansoft.proximity.model;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author
 *
 * A class to represent a single comment to a place
 */
public class CommentDesc {
	public long creationTime;

	public int rating;

	public String text;

	public String user;

	public String image;

	public int categoryId;
	
	public CommentDesc() {
	}

	public static CommentDesc fromJSON(JSONObject json)  throws RuntimeException,
	NoSuchMethodException, InstantiationException,
	IllegalAccessException, InvocationTargetException, JSONException{
		CommentDesc ret = ModelUtils.getObjectFromJSON(json, CommentDesc.class);
		return ret;
	}
}
