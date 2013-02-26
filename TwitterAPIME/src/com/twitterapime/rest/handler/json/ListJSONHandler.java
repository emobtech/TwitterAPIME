/*
 * ListJSONHandler.java
 * 02/22/2013
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler.json;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.List;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.rest.handler.UserAccountHandler;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the list's JSON results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.9
 */
public final class ListJSONHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * List of lists.
	 * </p>
	 */
	private Vector lists = new Vector(10);

	/**
	 * <p>
	 * Create an instance of ListJSONHandler class.
	 * </p>
	 */
	public ListJSONHandler() {
		super("root");
		//
		lists = new Vector(10);
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		if (isArray(jsonObj, startKey)) {
			handleLists(jsonObj.getJSONArray(startKey));
		} else {
			handleList(jsonObj.getJSONObject(startKey));
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start lists parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleLists(JSONArray jsonObj) throws ParserException {
		for (int i = 0; i < jsonObj.length(); i++) {
			handleList(jsonObj.getJSONObject(i));
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start list parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleList(JSONObject jsonObj) throws ParserException {
		Hashtable listValues = new Hashtable(25);
		//
		putIf(listValues, MetadataSet.LIST_ID, jsonObj, "id");
		putIf(listValues, MetadataSet.LIST_NAME, jsonObj, "name");
		putIf(listValues, MetadataSet.LIST_FULL_NAME, jsonObj, "full_name");
		putIf(listValues, MetadataSet.LIST_SLUG, jsonObj, "slug");
		putIf(listValues, MetadataSet.LIST_DESCRIPTION, jsonObj, "description");
		putIf(listValues, MetadataSet.LIST_SUBSCRIBER_COUNT, jsonObj, "subscriber_count");
		putIf(listValues, MetadataSet.LIST_MEMBER_COUNT, jsonObj, "member_count");
		putIf(listValues, MetadataSet.LIST_URI, jsonObj, "uri");
		putIf(listValues, MetadataSet.LIST_FOLLOWING, jsonObj, "following");
		putIf(listValues, MetadataSet.LIST_MODE, jsonObj, "mode");
		//
		if (jsonObj.has("created_at")) {
			listValues.put(
				MetadataSet.LIST_CREATE_DATE,
				String.valueOf(StringUtil.convertTweetDateToLong(
					jsonObj.getString("created_at"))));
		}
		//
		if (jsonObj.has("user")) {
			Hashtable userValues = new Hashtable(25);
			//
			new UserAccountHandler().populate(
				userValues, jsonObj.getJSONObject("user"));
			//
			if (userValues.size() > 0) {
				listValues.put(
					MetadataSet.LIST_USER_ACCOUNT, new UserAccount(userValues));
			}
		}
		//
		lists.addElement(new List(listValues));
	}
	
	/**
	 * <p>
	 * Return the parsed lists.
	 * </p>
	 * @return Array of lists.
	 */
	public List[] getParsedLists() {
		List[] ts = new List[lists.size()];
		lists.copyInto(ts);
		//
		return ts;
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
