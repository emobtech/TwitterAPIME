/*
 * StatusJSONHandler.java
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
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.rest.handler.GeoLocationHandler;
import com.twitterapime.rest.handler.TweetHandler;
import com.twitterapime.rest.handler.UserAccountHandler;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.search.handler.TweetEntityHandler;

/**
 * <p>
 * Handler class for parsing the status' JSON results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.9
 */
public final class StatusJSONHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * List of tweets.
	 * </p>
	 */
	private Vector statusList;
	
	/**
	 * <p>
	 * List of statuses hash.
	 * </p>
	 */
	private Vector statusHashList;
	
	/**
	 * <p>
	 * Search device listener object.
	 * </p> 
	 */
	private SearchDeviceListener listener;

	/**
	 * <p>
	 * Create an instance of StatusJSONHandler class.
	 * </p>
	 */
	public StatusJSONHandler() {
		super("root");
		//
		statusList = new Vector(10);
		statusHashList = new Vector(10);
	}

	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		if (isArray(jsonObj, startKey)) {
			handleStatuses(jsonObj.getJSONArray(startKey));
		} else {
			jsonObj = jsonObj.getJSONObject(startKey);
			//
			if (jsonObj.has("statuses")) {
				handleStatuses(jsonObj.getJSONArray("statuses"));
			} else {
				handleStatus(jsonObj);
			}
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start statuses parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleStatuses(JSONArray jsonObj) throws ParserException {
		for (int i = 0; i < jsonObj.length(); i++) {
			handleStatus(jsonObj.getJSONObject(i));
		}
	}
	
	/**
	 * <p>
	 * Callback method called to notify to start status parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handleStatus(JSONObject jsonObj) throws ParserException {
		Hashtable statusValues = new Hashtable(10);
		TweetHandler tweetHandler = new TweetHandler();
		//
		tweetHandler.populate(statusValues, jsonObj);
		populateUser(statusValues, jsonObj);
		populateEntity(statusValues, jsonObj);
		populateLocation(statusValues, jsonObj);
		//
		populateSender(statusValues, jsonObj);
		populateRecipient(statusValues, jsonObj);
		//
		if (jsonObj.has("retweeted_status")) {
			jsonObj = jsonObj.getJSONObject("retweeted_status");
			//
			Hashtable retweetValues = new Hashtable(25);
			//
			tweetHandler.populate(retweetValues, jsonObj);
			//
			if (retweetValues.size() > 0) {
				populateUser(retweetValues, jsonObj);
				populateEntity(retweetValues, jsonObj);
				populateLocation(retweetValues, jsonObj);
				//
				statusValues.put(
					MetadataSet.TWEET_REPOSTED_TWEET, new Tweet(retweetValues));
			}
		}
		//
		Tweet status = new Tweet(statusValues);
		//
		fireStatusParsed(status);
		//
		statusHashList.add(statusValues);
		statusList.add(status);
	}
	
	/**
	 * <p>
	 * Return the parsed tweets.
	 * </p>
	 * @return Array of tweets.
	 */
	public Tweet[] getParsedTweets() {
		Tweet[] ts = new Tweet[statusList.size()];
		statusList.copyInto(ts);
		//
		return ts;
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given tweet.
	 * </p>
	 * @param tweet Tweet to be loaded.
	 * @param index Tweet index.
	 */
	public void loadParsedTweet(Tweet tweet, int index) {
		tweet.setData((Hashtable)statusHashList.elementAt(index));
	}
	
	/**
	 * <p>
	 * Set the search device listener object.
	 * </p>
	 * @param listener Listener object.
	 */
	public void setSearchDeviceListener(SearchDeviceListener listener) {
		this.listener = listener;
	}
	
	/**
	 * <p>
	 * Fire the listened when a tweet is parsed.
	 * </p>
	 * @param tweet Parsed tweet.
	 */
	private void fireStatusParsed(Tweet tweet) {
		if (listener != null) {
			listener.tweetFound(tweet);
		}
	}
	
	/**
	 * Populate user data.
	 * @param tweetValues Tweet data.
	 * @param jsonObj JSON Object.
	 */
	private void populateUser(Hashtable tweetValues, JSONObject jsonObj) {
		if (jsonObj.has("user")) {
			Hashtable userValues = new Hashtable(25);
			//
			new UserAccountHandler().populate(
				userValues, jsonObj.getJSONObject("user"));
			//
			if (userValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_USER_ACCOUNT,
					new UserAccount(userValues));
			}
		}
	}

	/**
	 * Populate entity data.
	 * @param tweetValues Tweet data.
	 * @param jsonObj JSON Object.
	 */
	private void populateEntity(Hashtable tweetValues, JSONObject jsonObj) {
		if (jsonObj.has("entities")) {
			Hashtable entityValues = new Hashtable(3);
			//
			new TweetEntityHandler().populate(
				entityValues, jsonObj.getJSONObject("entities"));
			//
			if (entityValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_ENTITY, new TweetEntity(entityValues));
			}
		}
	}

	/**
	 * Populate location data.
	 * @param tweetValues Tweet data.
	 * @param jsonObj JSON Object.
	 */
	private void populateLocation(Hashtable tweetValues, JSONObject jsonObj) {
		Hashtable geoValues = new Hashtable(5);
		GeoLocationHandler handler = new GeoLocationHandler();
		//
		if (jsonObj.has("geo")
				&& !jsonObj.get("geo").toString().equals("null")) {
			//
			handler.populate(geoValues, jsonObj.getJSONObject("geo"));
		}
		if (jsonObj.has("place")
				&& !jsonObj.get("place").toString().equals("null")) {
			//
			handler.populate(geoValues, jsonObj.getJSONObject("place"));
		}
		//
		if (geoValues.size() > 0) {
			tweetValues.put(
				MetadataSet.TWEET_LOCATION, new GeoLocation(geoValues));
		}
	}
	
	/**
	 * Populate sender data.
	 * @param tweetValues Tweet data.
	 * @param jsonObj JSON Object.
	 */
	private void populateSender(Hashtable tweetValues, JSONObject jsonObj) {
		if (jsonObj.has("sender")) {
			Hashtable senderValues = new Hashtable(3);
			//
			new UserAccountHandler().populate(
				senderValues, jsonObj.getJSONObject("sender"));
			//
			if (senderValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_USER_ACCOUNT,
					new UserAccount(senderValues));
			}
		}
	}

	/**
	 * Populate recipient data.
	 * @param tweetValues Tweet data.
	 * @param jsonObj JSON Object.
	 */
	private void populateRecipient(Hashtable tweetValues, JSONObject jsonObj) {
		if (jsonObj.has("recipient")) {
			Hashtable recipientValues = new Hashtable(3);
			//
			new UserAccountHandler().populate(
				recipientValues, jsonObj.getJSONObject("recipient"));
			//
			if (recipientValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_RECIPIENT_ACCOUNT,
					new UserAccount(recipientValues));
			}
		}
	}
}
