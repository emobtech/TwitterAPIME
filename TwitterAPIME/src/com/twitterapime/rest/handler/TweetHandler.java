/*
 * TweetHandler.java
 * 23/11/2009
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
 * Handler class for parsing the XML tweet from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class TweetHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/created_at")) {
			data.put(
				MetadataSet.TWEET_PUBLISH_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (path.endsWith("/id")) {
			data.put(MetadataSet.TWEET_ID, text);
		} else if (path.endsWith("/text")) {
			data.put(MetadataSet.TWEET_CONTENT, text);
		} else if (path.endsWith("/source")) {
			data.put(MetadataSet.TWEET_SOURCE, StringUtil.removeTags(text));
		} else if (path.endsWith("/favorited")) {
			data.put(MetadataSet.TWEET_FAVOURITE, text);
		} else if (path.endsWith("/in_reply_to_status_id")) {
			data.put(MetadataSet.TWEET_IN_REPLY_TO_TWEET_ID, text);
		}
	}
	
	/**
	 * <p>
	 * Populate the given hash according to the keys and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param jsonObj JSON object that contains tweet's data.
	 */
	public void populate(Hashtable data, JSONObject jsonObj) {
		putIf(data, MetadataSet.TWEET_ID, jsonObj, "id");
		putIf(data, MetadataSet.TWEET_CONTENT, jsonObj, "text");
		putIf(data, MetadataSet.TWEET_FAVOURITE, jsonObj, "favorited");
		putIf(data, MetadataSet.TWEET_IN_REPLY_TO_TWEET_ID, jsonObj, "in_reply_to_status_id");
		//
		if (jsonObj.has("created_at")) {
			data.put(MetadataSet.TWEET_PUBLISH_DATE, String.valueOf(StringUtil.convertTweetDateToLong(jsonObj.getString("created_at"))));
		}
		if (jsonObj.has("source")) {
			data.put(MetadataSet.TWEET_SOURCE, StringUtil.removeTags(jsonObj.getString("source")));
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