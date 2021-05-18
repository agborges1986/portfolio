package com.kaymansoft.proximity.model;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 *
 * A class to represent the users's preferences. When returned it also specifies the session id for the user.
 */
public class UserData {
	public String sessionId;

	public String userName;

	public String displayName;

	public int maximumDistance;

	public String image;

	public int[] preferedCategories;

	public static UserData fromJSON(JSONObject json) throws RuntimeException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, JSONException {
		UserData ret = ModelUtils.getObjectFromJSON(json, UserData.class);
		return ret;
	}
}
