/*
 * GeoLocationHandler.java
 * 18/08/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML geo location from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.4
 */
public final class GeoLocationHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/georss:point")) {
			String[] values = StringUtil.split(text, ' ');
			//
			data.put(MetadataSet.GEOLOCATION_LATITUDE, values[0]);
			data.put(MetadataSet.GEOLOCATION_LONGITUDE, values[1]);
		} else if (path.endsWith("/id")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_ID, text);
		} else if (path.endsWith("/name")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_NAME, text);
		} else if (path.endsWith("/full_name")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_FULL_NAME, text);
		} else if (path.endsWith("/place_type")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_TYPE, text);
		} else if (path.endsWith("/url")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_URL, text);
		} else if (path.endsWith("/georss:polygon")) {
			data.put(
				MetadataSet.GEOLOCATION_POLYGON, StringUtil.split(text, ' '));
		} else if (path.endsWith("/country")) {
			data.put(MetadataSet.GEOLOCATION_COUNTRY, text);
		}
	}
	
	/**
	 * <p>
	 * Populate the given hash according to the keys and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param jsonObj JSON object that contains user's data.
	 */
	public void populate(Hashtable data, JSONObject jsonObj) {
		putIf(data, MetadataSet.GEOLOCATION_PLACE_ID, jsonObj, "id");
		putIf(data, MetadataSet.GEOLOCATION_PLACE_NAME, jsonObj, "name");
		putIf(data,MetadataSet.GEOLOCATION_PLACE_FULL_NAME,jsonObj,"full_name");
		putIf(data, MetadataSet.GEOLOCATION_PLACE_TYPE, jsonObj, "place_type");
		putIf(data, MetadataSet.GEOLOCATION_PLACE_URL, jsonObj, "url");
		putIf(data, MetadataSet.GEOLOCATION_COUNTRY, jsonObj, "country");
		//
		if (jsonObj.has("coordinates")) {
			JSONArray coord = jsonObj.getJSONArray("coordinates");
			//
			if (coord.length() >= 2) {
				data.put(MetadataSet.GEOLOCATION_LATITUDE, coord.getString(0));
				data.put(MetadataSet.GEOLOCATION_LONGITUDE, coord.getString(1));
			}
		}
		//
		if (jsonObj.has("polylines")) {
			JSONArray lines = jsonObj.getJSONArray("polylines");
			//
			Vector vector = new Vector();
			//
			for (int i = 0; i < lines.length(); i++) {
				vector.add(lines.getString(i));
			}
			//
			String[] array = new String[vector.size()];
			vector.copyInto(array);
			//
			data.put(MetadataSet.GEOLOCATION_POLYGON, array);
		}
	}

	/**
	 * <p>
	 * Put the given key's value in hash table if present in json object.
	 * </p>
	 * @param data Table.
	 * @param dataKey Table's key.
	 * @param jsonObj JSON object.
	 * @param jsonKey JSON's key.
	 */
	private void putIf(Hashtable data, String dataKey, JSONObject jsonObj,
		String jsonKey) {
		if (jsonObj.has(jsonKey)) {
			data.put(dataKey, jsonObj.getString(jsonKey));
		}
	}
}