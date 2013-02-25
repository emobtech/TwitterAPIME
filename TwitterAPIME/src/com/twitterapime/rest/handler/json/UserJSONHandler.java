/*
 * UserJSONHandler.java
 * 02/21/2013
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
import com.twitterapime.rest.UserAccount;
import com.twitterapime.rest.handler.TweetHandler;
import com.twitterapime.rest.handler.UserAccountHandler;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the user's JSON results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.9
 */
public final class UserJSONHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * List of users.
	 * </p>
	 */
	private Vector userList;
	
	/**
	 * <p>
	 * List of users hash.
	 * </p>
	 */
	private Vector userHashList;
	
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
	public UserJSONHandler() {
		super("root");
		//
		userList = new Vector(10);
		userHashList = new Vector(10);
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		if (isArray(jsonObj, startKey)) {
			handleAccounts(jsonObj.getJSONArray(startKey));
		} else {
			jsonObj = jsonObj.getJSONObject(startKey);
			//
			if (jsonObj.has("users")) {
				nextCursorIndex =
					Long.parseLong(jsonObj.getString("next_cursor"));
				prevCursorIndex =
					Long.parseLong(jsonObj.getString("previous_cursor"));
				//
				handleAccounts(jsonObj.getJSONArray("users"));
			} else {
				handleAccount(jsonObj);
			}
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
			handleAccount(jsonObj.getJSONObject(i));
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start account parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleAccount(JSONObject jsonObj) throws ParserException {
		Hashtable accountValues = new Hashtable(25);
		Hashtable lastTweetValues = new Hashtable(5);
		//
		new UserAccountHandler().populate(accountValues, jsonObj);
		//
		if (jsonObj.has("status")) {
			new TweetHandler().populate(
				lastTweetValues, jsonObj.getJSONObject("status"));	
			//
			if (lastTweetValues.size() > 0) {
				accountValues.put(
					MetadataSet.USERACCOUNT_LAST_TWEET,
					new Tweet(lastTweetValues));
			}
		}
		//
		userHashList.add(accountValues);
		userList.add(new UserAccount(accountValues));
	}
	
	/**
	 * <p>
	 * Return the parsed user accounts.
	 * </p>
	 * @return User accounts.
	 */
	public UserAccount[] getParsedUserAccounts() {
		UserAccount[] users = new UserAccount[userList.size()];
		userList.copyInto(users);
		//
		return users;
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given UserAccount.
	 * </p>
	 * @param user UserAccount to be loaded.
	 * @param index UserAccount index.
	 */
	public void loadParsedUserAccount(UserAccount user, int index) {
		user.setData((Hashtable)userHashList.elementAt(index));
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
}
