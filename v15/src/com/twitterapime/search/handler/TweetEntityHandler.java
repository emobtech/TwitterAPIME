/*
 * TrendTopicsHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.search.TweetEntity;

/**
 * <p>
 * Handler class for parsing the XML tweet's entity from Twitter API.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public final class TweetEntityHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Hold user data from mentions tag.
	 * </p>
	 */
	private Hashtable userAccountData;

	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/user_mentions/user_mention/id")) {
			Vector v =
				getOrCreateIfNecessary(data, MetadataSet.TWEETENTITY_MENTIONS);
			//
			userAccountData = new Hashtable();
			userAccountData.put(MetadataSet.TWEETENTITY_USERACCOUNT_ID, text);
			//
			v.addElement(new TweetEntity(userAccountData));
		} else if (path.endsWith("/user_mentions/user_mention/screen_name")) {
			if (userAccountData != null) {
				userAccountData.put(
					MetadataSet.TWEETENTITY_USERACCOUNT_USER_NAME, text);
			}
		} else if (path.endsWith("/user_mentions/user_mention/name")) {
			if (userAccountData != null) {
				userAccountData.put(
					MetadataSet.TWEETENTITY_USERACCOUNT_NAME, text);
			}
		} else if (path.endsWith("/urls/url/url")) {
			Vector v =
				getOrCreateIfNecessary(data, MetadataSet.TWEETENTITY_URLS);
			//
			Hashtable t = new Hashtable();
			t.put(MetadataSet.TWEETENTITY_URL, text);
			//
			v.addElement(new TweetEntity(t));
		} else if (path.endsWith("/hashtags/hashtag/text")) {
			Vector v =
				getOrCreateIfNecessary(data, MetadataSet.TWEETENTITY_HASHTAGS);
			//
			Hashtable t = new Hashtable();
			t.put(MetadataSet.TWEETENTITY_HASHTAG, text);
			//
			v.addElement(new TweetEntity(t));
		}
	}
	
	/**
	 * <p>
	 * Get (and create if necessary) a vector for associated to a given key.
	 * </p>
	 * @param data Data.
	 * @param key Key.
	 * @return Vector.
	 */
	private Vector getOrCreateIfNecessary(Hashtable data, String key) {
		Vector v = (Vector)data.get(key);
		//
		if (v == null) {
			v = new Vector();
			data.put(key, v);
		}
		//
		return v;
	}
}
