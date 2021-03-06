/*
 * Timeline.java
 * 11/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Handler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.TimelineHandler;
import com.twitterapime.rest.handler.json.StatusJSONHandler;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines the methods responsible for accessing the main timelines
 * (i.e. public, home, user, mentions and DMs) provided by Twitter.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   Timeline ter = Timeline.getInstance(uam);
 * 
 *   Query q = QueryComposer.count(5); //return only 5 latest tweets.
 * 
 *   ter.startGetHomeTweets(q, new SearchDeviceListener() {
 *       public void searchCompleted() {}
 *       public void searchFailed(Throwable cause) {}
 *       public void tweetFound(Tweet tweet) {
 *           System.out.println(tweet);
 *       }
 *   });
 * }
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.6
 * @since 1.2
 * @see UserAccountManager
 * @see SearchDeviceListener
 * @see QueryComposer
 */
public final class Timeline {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;
	
	/**
	 * <p>
	 * Timeline pool used to cache instanced associated to user accounts.
	 * </p>
	 */
	private static Hashtable timelinePool;
	
	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static Timeline singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service statuses public timeline.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/statuses/public_timeline" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/statuses/public_timeline
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetPublicTweets(SearchDeviceListener)
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses home timeline.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetHomeTweets(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses user timeline.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetUserTweets(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses mentions.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions_timeline" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/statuses/mentions_timeline
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetMentions(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_MENTIONS =
		"TWITTER_API_URL_SERVICE_STATUSES_MENTIONS";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses retweets of me.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetRetweetsOfMe(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_RETWEETS_OF_ME =
		"TWITTER_API_URL_SERVICE_STATUSES_RETWEETS_OF_ME";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses retweeted by me.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/statuses/retweeted_by_me" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/statuses/retweeted_by_me
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetRetweetsByMe(Query, SearchDeviceListener)
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_BY_ME
		= "TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_BY_ME";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses retweeted to me.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/statuses/retweeted_to_me" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/statuses/retweeted_to_me
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetRetweetsToMe(Query, SearchDeviceListener)
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_TO_ME
		= "TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_TO_ME";

	/**
	 * <p>
	 * Key for Twitter API URL service direct messages.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/direct_messages
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetDirectMessages(Query, boolean, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_DIRECT_MESSAGES =
		"TWITTER_API_URL_SERVICE_DIRECT_MESSAGES";

	/**
	 * <p>
	 * Key for Twitter API URL service direct messages sent.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages/sent" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/direct_messages/sent
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetDirectMessages(Query, boolean, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT =
		"TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists statuses.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/%3Auser/lists/%3Aid/statuses" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/%3Auser/lists/%3Aid/statuses
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_STATUSES =
		"TWITTER_API_URL_SERVICE_USER_LISTS_STATUSES";

	/**
	 * <p>
	 * Key for Twitter API URL service favorite statuses.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/favorites/list" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/favorites/list
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetFavoriteTweets(Query, SearchDeviceListener)
	 * @see Timeline#startGetFavoriteTweets(UserAccount, Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_FAVORITES =
		"TWITTER_API_URL_SERVICE_FAVORITES";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists statuses.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/lists/statuses
	 * </a>
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetListTweets(List, Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_STATUSES =
		"TWITTER_API_URL_SERVICE_LISTS_STATUSES";

	static {
		SERVICES_URL = new Hashtable(11);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE,
			"http://api.twitter.com/1/statuses/public_timeline.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE,
			"https://api.twitter.com/1.1/statuses/home_timeline.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE,
			"https://api.twitter.com/1.1/statuses/user_timeline.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_MENTIONS,
			"https://api.twitter.com/1.1/statuses/mentions_timeline.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_DIRECT_MESSAGES,
			"https://api.twitter.com/1.1/direct_messages.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT,
			"https://api.twitter.com/1.1/direct_messages/sent.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_RETWEETS_OF_ME,
			"https://api.twitter.com/1.1/statuses/retweets_of_me.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_BY_ME,
			"http://api.twitter.com/1/statuses/retweeted_by_me.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_TO_ME,
			"http://api.twitter.com/1/statuses/retweeted_to_me.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FAVORITES,
			"https://api.twitter.com/1.1/favorites/list.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_STATUSES,
			"https://api.twitter.com/1.1/lists/statuses.json");
	}
	
	/**
	 * <p>
	 * Get an URL related to the given service key.
	 * </p>
	 * @param serviceKey Service key.
	 * @return URL.
	 */
	private String getURL(String serviceKey) {
		return (String)SERVICES_URL.get(serviceKey);
	}

	/**
	 * <p>
	 * Release the objects which account is no longer authenticated.
	 * </p>
	 */
	static synchronized void cleanPool() {
		if (timelinePool != null) {
			Enumeration keys = timelinePool.keys();
			Object key;
			Timeline value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (Timeline)timelinePool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					timelinePool.remove(key);
				}
			}			
		}
	}
	
	/**
	 * <p>
	 * Set a new URL to a given Twitter API service. This method is very useful
	 * in case Twitter API decides to change a service's URL. So there is no
	 * need to wait for a new version of this API to get it working back.
	 * </p>
	 * <p>
	 * <b>Be careful about using this method, since it can cause unexpected
	 * results, case you enter an invalid URL.</b>
	 * </p>
	 * @param serviceKey Service key.
	 * @param url New URL.
	 * @see Timeline#TWITTER_API_URL_SERVICE_DIRECT_MESSAGES
	 * @see Timeline#TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_MENTIONS
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_RETWEETS_OF_ME
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_BY_ME
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_TO_ME
	 * @see Timeline#TWITTER_API_URL_SERVICE_FAVORITES
	 * @see Timeline#TWITTER_API_URL_SERVICE_LISTS_STATUSES
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of Timeline class and associate it to a given user
	 * account.
	 * </p>
	 * @param uam User account manager.
	 * @return Timeline instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static Timeline getInstance(UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (timelinePool == null) {
			timelinePool = new Hashtable();
		}
		//
		Timeline ter = (Timeline)timelinePool.get(uam);
		if (ter == null) {
			ter = new Timeline(uam);
			timelinePool.put(uam, ter);
		}
		//
		return ter;
	}

	/**
	 * <p>
	 * Get a single instance of Timeline class, which is NOT associated to any
	 * user account.
	 * </p>
	 * @return Timeline single instance.
	 * @deprecated Twitter API v1.1 now requires authentication for all methods.
	 */
	public synchronized static Timeline getInstance() {
		if (singleInstance == null) {
			singleInstance = new Timeline();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

	/**
	 * <p>
	 * Create an instance of Timeline class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Timeline() {
	}
	
	/**
	 * <p>
	 * Create an instance of Timeline class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private Timeline(UserAccountManager uam) {
		userAccountMngr = uam;
	}
	
	/**
	 * <p>
	 * Get most recent tweets from non-protected users who have set a custom
	 * user icon.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * </ul>
	 * </p>
	 * @param l Listener object to be notified about the search's result.
	 * @throws IllegalArgumentException If listener is null.
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public void startGetPublicTweets(SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(
			TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE,
			QueryComposer.includeEntities(),
			l,
			h,
			false);
	}
	
	/**
	 * <p>
	 * Get most recent tweets, including retweets, posted by the
	 * authenticating user and the user's friends according to given filter.
	 * This is the equivalent to timeline home on the Web.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetHomeTweets(Query q, SearchDeviceListener l) {
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get most recent tweets posted from the authenticating user and the user's
	 * friends according to given filter. This is the equivalent of the Web user
	 * page for your own user.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userID(String)}</li>
	 * <li>{@link QueryComposer#screenName(String)}</li>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeRetweets()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetUserTweets(Query q, SearchDeviceListener l) {
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get most recent mentions (status containing @username) for the
	 * authenticating user according to given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeRetweets()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetMentions(Query q, SearchDeviceListener l) {
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_MENTIONS, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get all Direct Messages from/to the authenticating user according to
	 * given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all DMs are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @param received Return received DMs (true), otherwise, sent ones.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetDirectMessages(Query q, boolean received, 
		SearchDeviceListener l) {
		final String urlKey = 
			received
				? TWITTER_API_URL_SERVICE_DIRECT_MESSAGES
				: TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT;
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(urlKey, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get most recent tweets retweeted by authenticating user, according to 
	 * given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public void startGetRetweetsByMe(Query q, SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_BY_ME, q, l,h,true);
	}
		
	/**
	 * <p>
	 * Get most recent tweets retweeted by users followed by authenticating 
	 * user, according to given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public void startGetRetweetsToMe(Query q, SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_RETWEETED_TO_ME, q, l,h,true);
	}

	/**
	 * <p>
	 * Get most recent tweets by authenticating user that were reposted by
	 * other users, according to given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#trimUser()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetRetweetsOfMe(Query q, SearchDeviceListener l) {
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_RETWEETS_OF_ME, q, l, h,true);
	}
	
	/**
	 * <p>
	 * Get tweets from users who belongs to a given list, according to given 
	 * filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#perPage(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * <li>{@link QueryComposer#includeRetweets()}</li></ul>
	 * </p>
	 * @param list List object.
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If the list is private or the user is not
	 *                           authenticated.
	 * @throws IllegalArgumentException If List and/or listener is null.
	 */
	public void startGetListTweets(List list, Query q, SearchDeviceListener l) {
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		String url = getURL(TWITTER_API_URL_SERVICE_LISTS_STATUSES);
		Query qry =	new Query("list_id=" + list.getString(MetadataSet.LIST_ID));
		//
		if (q != null) {
			q = QueryComposer.append(qry, q);
		} else {
			q = qry;
		}
		//
		startGet(url, q, l, h, true);
	}

	/**
	 * <p>
	 * Get most recent favorite tweets for the authenticating user, according to
	 * given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetFavoriteTweets(Query q, SearchDeviceListener l) {
		String username =
			userAccountMngr.getCredential().getString(
				MetadataSet.CREDENTIAL_USERNAME);
		//
		startGetFavoriteTweets(new UserAccount(username), q, l);
	}

	/**
	 * <p>
	 * Get most recent favorite tweets for the given user, according to given
	 * filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param user User account.
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If the given user is protected or user is not 
	 *                           authenticated.
	 * @throws IllegalArgumentException If user or listener is null.
	 */
	public void startGetFavoriteTweets(UserAccount user, Query q,
		SearchDeviceListener l) {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null");
		}
		//
		if (StringUtil.isEmpty(user.getString(MetadataSet.USERACCOUNT_ID))) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		StatusJSONHandler h = new StatusJSONHandler();
		h.setSearchDeviceListener(l);
		//
		String url = getURL(TWITTER_API_URL_SERVICE_FAVORITES);
		//
		String[] pv = user.getUserNameOrIDParamValue();
		String param = "?" + pv[0] + "=" + pv[1];
		//
		url += param;
		//
		startGet(url, q, l, h, true);
	}

	/**
	 * <p>
	 * Start a retrieval of a given URL's response according to a filter.
	 * </p>
	 * @param servURLKey The service URL key.
	 * @param q The filter query.
	 * @param l Listener object to be notified about the search's result.
	 * @param h Content handler.
	 * @param checkAuth Authentication required (true).
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException Invalid parameters.
	 */
	private void startGet(final String servURLKey, final Query q,
		final SearchDeviceListener l, final Handler h, final boolean checkAuth){
		if (servURLKey == null || servURLKey.trim().length() == 0) {
			throw new IllegalArgumentException("Url must not be empty/null.");
		}
		if (l == null) {
			throw new IllegalArgumentException("Listener must not be null.");
		}
		if (h == null) {
			throw new IllegalArgumentException("Handler must not be null.");
		}
		//
		checkUserAuth();
		//
		Runnable r = new Runnable() {
			public void run() {
				HttpRequest req;
				String url = servURLKey;
				//
				if (!servURLKey.startsWith("http")) {
					url = getURL(servURLKey);
				}
				url = q != null ? url + '?' + q.toString() : url;
				//
				if (userAccountMngr != null) {
					req = userAccountMngr.createRequest(url);
				} else {
					req = new HttpRequest(url);
				}
				//
				try {
					HttpResponse resp = req.send();
					//
					HttpResponseCodeInterpreter.perform(resp);
					//
					Parser parser = ParserFactory.getParser(ParserFactory.JSON);
					parser.parse(resp.getStream(), h);
					//
					l.searchCompleted();
				} catch (Exception e) {
					l.searchFailed(e);
				} finally {
					try {
						req.close();
					} catch (IOException e) {}
				}
			}
		};
		//
		Thread t = new Thread(r);
		t.start();
	}
	
	/**
	 * <p>
	 * Check if the user's is authenticated.
	 * </p>
	 * @throws SecurityException User is not authenticated.
	 */
	private void checkUserAuth() {
		if (userAccountMngr == null || !userAccountMngr.isVerified()) {
			throw new SecurityException(
			    "User's credential must be entered to perform this operation.");
		}
	}
}
