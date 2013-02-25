/*
 * UserAccountHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML user account from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.4
 * @since 1.1
 */
public final class UserAccountHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/id")) {
			data.put(MetadataSet.USERACCOUNT_ID, text);
		} else if (path.endsWith("/name")) {
			data.put(MetadataSet.USERACCOUNT_NAME, text);
		} else if (path.endsWith("/screen_name")) {
			data.put(MetadataSet.USERACCOUNT_USER_NAME, text);
			//
			final String picUri =
				"http://api.twitter.com/1/users/profile_image/" + 
				text + 
				".json?size=";
			//
			data.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_MINI, picUri + "mini");
			data.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_NORMAL, picUri + "normal");
			data.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_BIGGER, picUri + "bigger");
		} else if (path.endsWith("/location")) {
			data.put(MetadataSet.USERACCOUNT_LOCATION, text);
		} else if (path.endsWith("/description")) {
			data.put(MetadataSet.USERACCOUNT_DESCRIPTION, text);
		} else if (path.endsWith("/profile_image_url")) {
			data.put(MetadataSet.USERACCOUNT_PICTURE_URI, text);
		} else if (path.endsWith("/url")) {
			data.put(MetadataSet.USERACCOUNT_URL, text);
		} else if (path.endsWith("/protected")) {
			data.put(MetadataSet.USERACCOUNT_PROTECTED, text);
		} else if (path.endsWith("/followers_count")) {
			data.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, text);
		} else if (path.endsWith("/profile_background_color")) {
			data.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, text);
		} else if (path.endsWith("/profile_text_color")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, text);
		} else if (path.endsWith("/profile_link_color")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, text);
		} else if (path.endsWith("/friends_count")) {
			data.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, text);
		} else if (path.endsWith("/created_at")) {
			data.put(
				MetadataSet.USERACCOUNT_CREATE_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (path.endsWith("/favourites_count")) {
			data.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, text);
		} else if (path.endsWith("/utc_offset")) {
			data.put(MetadataSet.USERACCOUNT_UTC_OFFSET, text);
		} else if (path.endsWith("/time_zone")) {
			data.put(MetadataSet.USERACCOUNT_TIME_ZONE, text);
		} else if (path.endsWith("/profile_background_image_url")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI,text);
		} else if (path.endsWith("/statuses_count")) {
			data.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, text);
		} else if (path.endsWith("/notifications")) {
			data.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, text);
		} else if (path.endsWith("/verified")) {
			data.put(MetadataSet.USERACCOUNT_VERIFIED, text);
		} else if (path.endsWith("/geo_enabled")) {
			data.put(MetadataSet.USERACCOUNT_GEO_ENABLED, text);
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
		putIf(data, MetadataSet.USERACCOUNT_ID, jsonObj, "id");
		putIf(data, MetadataSet.USERACCOUNT_NAME, jsonObj, "name");
		putIf(data, MetadataSet.USERACCOUNT_USER_NAME, jsonObj, "screen_name");
		putIf(data, MetadataSet.USERACCOUNT_LOCATION, jsonObj, "location");
		putIf(data, MetadataSet.USERACCOUNT_DESCRIPTION, jsonObj, "description");
		putIf(data, MetadataSet.USERACCOUNT_PICTURE_URI, jsonObj, "profile_image_url");
		putIf(data, MetadataSet.USERACCOUNT_URL, jsonObj, "url");
		putIf(data, MetadataSet.USERACCOUNT_UTC_OFFSET, jsonObj, "utc_offset");
		putIf(data, MetadataSet.USERACCOUNT_TIME_ZONE, jsonObj, "time_zone");
		putIf(data, MetadataSet.USERACCOUNT_PROTECTED, jsonObj, "protected");
		putIf(data, MetadataSet.USERACCOUNT_NOTIFICATIONS, jsonObj, "notifications");
		putIf(data, MetadataSet.USERACCOUNT_VERIFIED, jsonObj, "verified");
		putIf(data, MetadataSet.USERACCOUNT_GEO_ENABLED, jsonObj, "geo_enabled");
		putIf(data, MetadataSet.USERACCOUNT_TWEETS_COUNT, jsonObj, "statuses_count");
		putIf(data, MetadataSet.USERACCOUNT_FAVOURITES_COUNT, jsonObj, "favourites_count");
		putIf(data, MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, jsonObj, "followers_count");
		putIf(data, MetadataSet.USERACCOUNT_FRIENDS_COUNT, jsonObj, "friends_count");
		putIf(data, MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, jsonObj, "profile_background_color");
		putIf(data, MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, jsonObj, "profile_text_color");
		putIf(data, MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, jsonObj, "profile_link_color");
		putIf(data, MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, jsonObj, "profile_background_image_url");
		//
		if (jsonObj.has("created_at")) {
			data.put(MetadataSet.USERACCOUNT_CREATE_DATE, String.valueOf(StringUtil.convertTweetDateToLong(jsonObj.getString("created_at"))));
		}
		if (jsonObj.has("screen_name")) {
			final String picUri =
				"http://api.twitter.com/1/users/profile_image/" + 
				jsonObj.getString("screen_name") + 
				".json?size=";
			//
			data.put(MetadataSet.USERACCOUNT_PICTURE_URI_MINI, picUri + "mini");
			data.put(MetadataSet.USERACCOUNT_PICTURE_URI_NORMAL, picUri + "normal");
			data.put(MetadataSet.USERACCOUNT_PICTURE_URI_BIGGER, picUri + "bigger");
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