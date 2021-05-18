package com.kaymansoft.proximity.model;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 *
 * A class to represent a single category in use by the server.
 */
public class CategoryDesc {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int id;
	public String description;
	public String name;

	public static CategoryDesc fromJSON(JSONObject json)
			throws RuntimeException, IllegalArgumentException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, JSONException {
		CategoryDesc ret = ModelUtils.getObjectFromJSON(json, CategoryDesc.class);
		return ret;
	}

	public static CategoryDesc[] fromJSON(JSONArray json)
			throws RuntimeException, IllegalArgumentException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, JSONException {
		CategoryDesc[] ret = new CategoryDesc[json.length()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ModelUtils.getObjectFromJSON(json.getJSONObject(i),
					CategoryDesc.class);
		}
		return ret;
	}
}
