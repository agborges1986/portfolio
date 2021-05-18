package com.kaymansoft.proximity.model;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelUtils {

	static <T> T getObjectFromJSON(JSONObject json, Class<T> clazz)
			throws RuntimeException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException, JSONException {
		Constructor<T> cns = clazz.getConstructor();
		T ret = cns.newInstance();
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			if (field.getType().equals(String.class)) {
				field.set(ret, json.getString(field.getName()));
			} else if (field.getType().isPrimitive()) {
				if (field.getType().equals(int.class)) {
					field.set(ret, json.optInt(field.getName()));
				} else if (field.getType().equals(long.class)) {
					field.set(ret, json.optLong(field.getName()));
				} else if (field.getType().equals(double.class)) {
					field.set(ret, json.optDouble(field.getName()));
				}
			} else if (field.getType().isArray()) {

				JSONArray array = json.optJSONArray(field.getName());
				if (field.getType().equals(int[].class)) {
					int[] arr;
					if (array == null) {
						if (json.has(field.getName())) {
							int tmp = json.optInt(field.getName());
							arr = new int[] { tmp };
						} else {
							arr = new int[0];
						}
					} else {
						arr = new int[array.length()];
						for (int j = 0; j < arr.length; j++) {
							arr[j] = array.getInt(j);
						}
					}
					field.set(ret, arr);
				} else if (field.getType().equals(String[].class)) {
					String[] arr;
					if (array == null) {
						if (json.has(field.getName())) {
							String tmp = json.optString(field.getName());
							arr = new String[] { tmp };
						} else {
							arr = new String[0];
						}
					} else {
						arr = new String[array.length()];
						for (int j = 0; j < arr.length; j++) {
							arr[j] = array.optString(j);
						}
					}
					field.set(ret, arr);
				} else {
					// TODO ignoring this for now...
				}
			} else {
				field.set(
						ret,
						getObjectFromJSON(json.getJSONObject(field.getName()),
								field.getType()));
			}
		}
		return ret;
	}
	
	public static JSONObject getJSONFromObject(Object object) throws JSONException, IllegalArgumentException, IllegalAccessException{
		JSONObject ret = new JSONObject();
		if(object.getClass().isArray()){
			JSONArray arr = getJSONArrayFromObject(object);
			ret.accumulate((object.getClass().getName().charAt(0)|32) + object.getClass().getName().substring(1), arr);
		}else{
			Field[] fields = object.getClass().getFields();
			for(int i = 0; i < fields.length; i++){
				Field field = fields[i];
				if(field.getType().equals(int.class)){
					ret.accumulate(field.getName(), field.getInt(object));
				}else if(field.getType().equals(long.class)){
					ret.accumulate(field.getName(), field.getLong(object));
				}else if(field.getType().equals(double.class)){
					ret.accumulate(field.getName(), field.getDouble(object));
				}else if(field.getType().equals(boolean.class)){
					ret.accumulate(field.getName(), field.getBoolean(object));
				}else if(field.getType().equals(String.class)){
					ret.accumulate(field.getName(), field.get(object));
				}else if(field.getType().isArray()){
					JSONArray arr = getJSONArrayFromObject(field.get(object));
					ret.accumulate(field.getName(), arr);
				}else {
					ret.accumulate(field.getName(), getJSONFromObject(field.get(object)));
				}
			}
		}
		return ret;
	}
	
	private static JSONArray getJSONArrayFromObject(Object object) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, JSONException, IllegalAccessException{
		JSONArray arr = new JSONArray();
		for(int i = 0 ; i < Array.getLength(object); i++){
			if(object.getClass().equals(int[].class)){
				arr.put(Array.getInt(object, i));
			}else if(object.getClass().equals(String[].class)){
				arr.put(Array.get(object, i));
			}else if(object.getClass().equals(double[].class)){
				arr.put(Array.getDouble(object, i));
			}else if(object.getClass().equals(long[].class)){
				arr.put(Array.getLong(object, i));
			}else if(object.getClass().equals(boolean[].class)){
				arr.put(Array.getBoolean(object, i));
			}else {
				arr.put(getJSONFromObject(Array.get(object, i)));
			}
		}
		return arr;
	}
}
