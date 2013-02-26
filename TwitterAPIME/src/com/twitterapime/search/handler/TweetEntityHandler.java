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
import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.search.TweetEntity;

/**
 * <p>
 * Handler class for parsing the XML tweet's entity from Twitter API.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.5
 */
public final class TweetEntityHandler {
	/**
	 * <p>
	 * Hold user data from mentions tag.
	 * </p>
	 */
	private Hashtable userAccountData;
	
	/**
	 * <p>
	 * Hold url data from urls tag
	 * </p>
	 */
	private Hashtable urlData;
	
	/**
	 * </p>
	 * Hold media data from the media tag
	 * </p>
	 */
	private Hashtable mediaData;

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
			urlData = new Hashtable();
			urlData.put(MetadataSet.TWEETENTITY_URL, text);
			//			
			v.addElement(new TweetEntity(urlData));
		} else if (path.endsWith("/urls/url/display_url")) {
			if (urlData != null) {
				urlData.put(MetadataSet.TWEETENTITY_DISPLAY_URL, text);
			}
		} else if (path.endsWith("/urls/url/expanded_url")) {
			if (urlData != null) {
				urlData.put(MetadataSet.TWEETENTITY_EXPANDED_URL, text);
			}
		} else if (path.endsWith("/media/creative/media_url")) {
			Vector v =
				getOrCreateIfNecessary(data, MetadataSet.TWEETENTITY_MEDIAS);
			mediaData = new Hashtable();
			mediaData.put(MetadataSet.TWEETENTITY_MEDIA, text);
			
			v.addElement(new TweetEntity(mediaData));
		} else if (path.endsWith("/media/creative/display_url")) {
			if (mediaData != null) {
				mediaData.put(MetadataSet.TWEETENTITY_DISPLAY_URL, text);
			}
		} else if (path.endsWith("/media/creative/url")) {
			if (mediaData != null) {
				mediaData.put(MetadataSet.TWEETENTITY_URL, text);
			}
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
	 * Populate the given hash according to the keys and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param jsonObj JSON object that contains tweet's data.
	 */
	public void populate(Hashtable data, JSONObject jsonObj) {
		if (jsonObj.has("user_mentions")) {
			JSONArray jsonMentions = jsonObj.getJSONArray("user_mentions");
			Vector mentions = new Vector(jsonMentions.length());
			//
			for (int i = 0; i < jsonMentions.length(); i++) {
				JSONObject obj = jsonMentions.getJSONObject(i);
				Hashtable t = new Hashtable();
				//
				putIf(t, MetadataSet.TWEETENTITY_USERACCOUNT_ID, obj, "id");
				putIf(t, MetadataSet.TWEETENTITY_USERACCOUNT_USER_NAME, obj, "screen_name");
				putIf(t, MetadataSet.TWEETENTITY_USERACCOUNT_NAME, obj, "name");
				//
				if (t.size() > 0) {
					mentions.addElement(new TweetEntity(t));
				}
			}
			//
			if (mentions.size() > 0) {
				data.put(MetadataSet.TWEETENTITY_MENTIONS, mentions);
			}
		}
		//
		if (jsonObj.has("media")) {
			JSONArray jsonMedias = jsonObj.getJSONArray("media");
			Vector medias = new Vector(jsonMedias.length());
			//
			for (int i = 0; i < jsonMedias.length(); i++) {
				JSONObject obj = jsonMedias.getJSONObject(i);
				Hashtable t = new Hashtable();
				//
				putIf(t, MetadataSet.TWEETENTITY_MEDIA, obj, "media_url");
				putIf(t, MetadataSet.TWEETENTITY_DISPLAY_URL, obj, "display_url");
				//
				if (t.size() > 0) {
					medias.addElement(new TweetEntity(t));
				}
			}
			//
			if (medias.size() > 0) {
				data.put(MetadataSet.TWEETENTITY_MEDIAS, medias);
			}
		}
		//
		if (jsonObj.has("urls")) {
			JSONArray jsonUrls = jsonObj.getJSONArray("urls");
			Vector urls = new Vector(jsonUrls.length());
			//
			for (int i = 0; i < jsonUrls.length(); i++) {
				JSONObject obj = jsonUrls.getJSONObject(i);
				Hashtable t = new Hashtable();
				//
				putIf(t, MetadataSet.TWEETENTITY_URL, obj, "url");
				putIf(t, MetadataSet.TWEETENTITY_DISPLAY_URL, obj, "display_url");
				putIf(t, MetadataSet.TWEETENTITY_EXPANDED_URL, obj, "expanded_url");
				//
				if (t.size() > 0) {
					urls.addElement(new TweetEntity(t));
				}
			}
			//
			if (urls.size() > 0) {
				data.put(MetadataSet.TWEETENTITY_URLS, urls);
			}
		}
		//
		if (jsonObj.has("hashtags")) {
			JSONArray jsonHashtags = jsonObj.getJSONArray("hashtags");
			Vector hashtags = new Vector(jsonHashtags.length());
			//
			for (int i = 0; i < jsonHashtags.length(); i++) {
				JSONObject obj = jsonHashtags.getJSONObject(i);
				Hashtable t = new Hashtable();
				//
				putIf(t, MetadataSet.TWEETENTITY_HASHTAG, obj, "text");
				//
				if (t.size() > 0) {
					hashtags.addElement(new TweetEntity(t));
				}
			}
			//
			if (hashtags.size() > 0) {
				data.put(MetadataSet.TWEETENTITY_HASHTAGS, hashtags);
			}
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
