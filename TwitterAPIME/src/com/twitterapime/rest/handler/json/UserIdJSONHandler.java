/*
 * UserIdJSONHandler.java
 * 02/25/2013
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler.json;

import java.util.Vector;

import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.parser.ParserException;

/**
 * <p>
 * Handler class for parsing the user id's JSON results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.9
 */
public final class UserIdJSONHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * IDs list.
	 * </p>
	 */
	private Vector idsList;
	
	/**
	 * <p>
	 * Max count of Ids.
	 * </p>
	 */
	private final long maxCount;
	
	/**
	 * <p>
	 * Next cursor index.
	 * </p>
	 */
	private long nextCursorIndex;

	/**
	 * <p>
	 * Previous cursor index.
	 * </p>
	 */
	private long prevCursorIndex;

	/**
	 * <p>
	 * Create an instance of AccountJSONHandler class.
	 * </p>
	 */
	public UserIdJSONHandler() {
		this(Long.MAX_VALUE);
	}
	
	/**
	 * <p>
	 * Create an instance of AccountJSONHandler class.
	 * </p>
	 * @param maxCount Max count of IDs.
	 */
	public UserIdJSONHandler(long maxCount) {
		super("root");
		//
		idsList = new Vector(10);
		this.maxCount = maxCount;
	}

	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		jsonObj = jsonObj.getJSONObject(startKey);
		//
		if (jsonObj.has("ids")) {
			nextCursorIndex =
				Long.parseLong(jsonObj.getString("next_cursor"));
			prevCursorIndex =
				Long.parseLong(jsonObj.getString("previous_cursor"));
			//
			handleAccounts(jsonObj.getJSONArray("ids"));
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start accounts parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleAccounts(JSONArray jsonObj) throws ParserException {
		for (int i = 0; i < jsonObj.length(); i++) {
			if (i >= maxCount) {
				break;
			}
			//
			idsList.add(jsonObj.get(i).toString());
		}
	}
	
	/**
	 * <p>
	 * Get the parsed ids.
	 * </p>
	 * @return Ids.
	 */
	public String[] getParsedIds() {
		String[] ids = new String[idsList.size()];
		idsList.copyInto(ids);
		//
		return ids;
	}
	
	/**
	 * <p>
	 * Return the next cursor index.
	 * </p>
	 * @return Index.
	 */
	public long getNextCursorIndex() {
		return nextCursorIndex;
	}

	/**
	 * <p>
	 * Return the previous cursor index.
	 * </p>
	 * @return Index.
	 */
	public long getPreviousCursorIndex() {
		return prevCursorIndex;
	}
	
	/**
	 * <p>
	 * Clear internal state of handler.
	 * </p>
	 */
	public void clear() {
		idsList.removeAllElements();
		nextCursorIndex = 0;
		prevCursorIndex = 0;
	}
}
