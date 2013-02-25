/*
 * UserAccountManager.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.Cursor;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.rest.handler.json.UserJSONHandler;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.XAuthSigner;

/**
 * <p>
 * This class is responsible for managing the user account.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c)
 * if (uam.verifyCredential()) {
 *   System.out.println("User logged in...");
 * }
 * uam.signOut();
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.7
 * @since 1.1
 */
public final class UserAccountManager {
	/**
	 * <p>
	 * UserAccountManager pool used to cache instanced associated to user
	 * credentials.
	 * </p>
	 */
	private static Hashtable userAccountMngrPoll;
	
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * Key for Twitter API URL service account verify credentials.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/account/verify_credentials" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/account/verify_credentials
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS";

	/**
	 * <p>
	 * Key for Twitter API URL service OAuth access token.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1/post/oauth/access_token" target="_blank">
	 *   https://dev.twitter.com/docs/api/1/post/oauth/access_token
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN =
		"TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN";
	
	/**
	 * <p>
	 * Key for Twitter API URL service account rate limit status.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/account/rate_limit_status" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/account/rate_limit_status
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getRateLimitStatus()
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service users show.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/users/show" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/users/show
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getUserAccount()
	 */
	public static final String TWITTER_API_URL_SERVICE_USERS_SHOW =
		"TWITTER_API_URL_SERVICE_USERS_SHOW";
	
	/**
	 * <p>
	 * Key for Twitter API URL service account update profile.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/post/account/update_profile
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getUserAccount()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE =
		"TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service report spam.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/post/users/report_spam" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/post/users/report_spam
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#reportSpam(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_REPORT_SPAM =
		"TWITTER_API_URL_SERVICE_REPORT_SPAM";

	/**
	 * <p>
	 * Key for Twitter API URL service users search.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/users/search" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/users/search
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#search(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_USERS_SEARCH =
		"TWITTER_API_URL_SERVICE_USERS_SEARCH";

	/**
	 * <p>
	 * Key for Twitter API URL service users lookup.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1.1/get/users/lookup" target="_blank">
	 *   https://dev.twitter.com/docs/api/1.1/get/users/lookup
	 * </a>
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#lookup(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_USERS_LOOKUP =
		"TWITTER_API_URL_SERVICE_USERS_LOOKUP";

	static {
		SERVICES_URL = new Hashtable(8);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS,
			"https://api.twitter.com/1.1/account/verify_credentials.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN,
			"https://api.twitter.com/oauth/access_token");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS,
			"http://api.twitter.com/1/account/rate_limit_status.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USERS_SHOW,
			"https://api.twitter.com/1.1/users/show.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE,
			"https://api.twitter.com/1.1/account/update_profile.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_REPORT_SPAM,
			"https://api.twitter.com/1.1/users/report_spam.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USERS_SEARCH,
			"https://api.twitter.com/1.1/users/search.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USERS_LOOKUP,
			"https://api.twitter.com/1.1/users/lookup.json");
	}

	/**
	 * <p>
	 * User's credentials.
	 * </p>
	 */
	private Credential credential;
	
	/**
	 * <p>
	 * Flag that indicates whether the user's credentials were verified.
	 * </p>
	 */
	private boolean verified;
	
	/**
	 * <p>
	 * Marks the instance as invalidated.
	 * </p>
	 */
	private boolean invalidated;
	
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private Token token;
	
	/**
	 * <p>
	 * XAuth signer instance.
	 * </p>
	 */
	private XAuthSigner signer;
	
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
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_USERS_SHOW
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_REPORT_SPAM
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_USERS_SEARCH
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_USERS_LOOKUP
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of UserAccountManager class and associate it to a given
	 * user credential.
	 * </p>
	 * @param c Credentials.
	 * @return AccountManager object.
	 * @throws IllegalArgumentException If credential is null.
	 */
	public synchronized static UserAccountManager getInstance(Credential c) {
		if (c == null) {
			throw new IllegalArgumentException("Credential must not be null.");
		}
		//
		UserAccountManager uam = null;
		//
		if (userAccountMngrPoll == null) {
			userAccountMngrPoll = new Hashtable();
		} else {
			synchronized (userAccountMngrPoll) {
				uam = (UserAccountManager)userAccountMngrPoll.get(c);
			}
		}
		//
		return uam != null ? uam : new UserAccountManager(c);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccountManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param c User's credential.
	 */
	private UserAccountManager(Credential c) {
		credential = c;
		//
		signer =
			new XAuthSigner(
				c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY),
				c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET));
	}

	/**
	 * <p>
	 * Returns a set of info about the number of API requests available to the
	 * requesting user before the REST API limit is reached for the current
	 * hour.
	 * </p>
	 * <p>
	 * Stay aware of these limits, since it can impact the usage of some methods
	 * of this API.
	 * </p>
	 * @return Rate limiting status info.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 * @deprecated No longer available in Twitter API v1.1.
	 */
	public RateLimitStatus getRateLimitStatus() throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS));
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			RateLimitStatusHandler handler = new RateLimitStatusHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedRateLimitStatus();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Return whether it is properly verified.
	 * </p>
	 * @return Verified (true).
	 */
	public boolean isVerified() {
		checkValid();
		//
		return verified;
	}
	
	/**
	 * <p>
	 * Verify whether the given user's credential are valid. This method
	 * authenticates the API to Twitter REST API.
	 * </p>
	 * @return Valid credentials (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public synchronized boolean verifyCredential() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			return true; //already verified.
		}
		//
		HttpRequest req;
		//
		token = credential.getAccessToken();
		//
		if (token == null) {
			req =
				createRequest(
					getURL(TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN));
			//
			signer.signForAccessToken(
				req,
				credential.getUsernameOrEmail(),
				credential.getString(MetadataSet.CREDENTIAL_PASSWORD));
			//
			try {
				HttpResponse resp = req.send();
				//
				if (resp.getCode() == HttpConnection.HTTP_OK) {
					token = Token.parse(resp.getBodyContent());
				} else if (resp.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
					return false;
				} else {
					HttpResponseCodeInterpreter.perform(resp);
					//
					return false;
				}
			} finally {
				req.close();
			}
		}
		//
		req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS));
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_OK) {
				UserJSONHandler handler = new UserJSONHandler();
				Parser parser = ParserFactory.getParser(ParserFactory.JSON);
				parser.parse(resp.getStream(), handler);
				UserAccount ua = handler.getParsedUserAccounts()[0];
				//
				credential.setUsername(
					ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
				//
				verified = true;
				saveSelfOnPool();
				//
				return true;
			} else if (resp.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
				token = null;
				//
				return false;
			} else {
				HttpResponseCodeInterpreter.perform(resp);
				//
				return false;
			}
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Returns the xAuth access token. This token is available only when xAuth
	 * authentication is performed. You can store this token for further use.
	 * Reusing this token allows you no longer request a given user credentials
	 * for a next log in operation. Just be aware that this token can expire or
	 * the user may revoke it.
	 * </p>
	 * @return Token.
	 */
	public Token getAccessToken() {
		return token;
	}
	
	/**
	 * <p>
	 * Ends the session of the authenticating user.
	 * </p>
	 * <p>
	 * Once signed out, this instance is no longer valid for use as well as
	 * another one dependent of it. Dump them!
	 * </p>
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public synchronized void signOut() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			verified = false;
			token = null;
			signer = null;
			userAccountMngrPoll.remove(credential);
			Timeline.cleanPool();
			TweetER.cleanPool();
			FriendshipManager.cleanPool();
			ListManager.cleanPool();
			//
			invalidated = true;
		}
	}
	
	/**
	 * <p>
	 * Get the logged user's account.
	 * </p>
	 * @return User account object.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount getUserAccount() throws IOException,
		LimitExceededException {
		return getUserAccount(
			new UserAccount(
				credential.getString(MetadataSet.CREDENTIAL_USERNAME)));
	}
	
	/**
	 * <p>
	 * Get the account of a given user.
	 * </p>
	 * @param user User.
	 * @return Username/id.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user/id/username is null/empty.
	 */
	public UserAccount getUserAccount(UserAccount user)
		throws IOException, LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		user.validateUserNameOrID();
		//
		String[] pv = user.getUserNameOrIDParamValue();
		String param = "?" + pv[0] + "=" + pv[1];
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_USERS_SHOW) + param);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			UserJSONHandler handler = new UserJSONHandler();
			parser.parse(resp.getStream(), handler);
			//
			handler.loadParsedUserAccount(user, 0);
			//
			return user;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}
	
	/**
	 * {@link FriendshipManager#follow(UserAccount)}
	 */
	public UserAccount follow(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).follow(ua);
	}
	
	/**
	 * {@link FriendshipManager#unfollow(UserAccount)}
	 */
	public UserAccount unfollow(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).unfollow(ua);
	}
	
	/**
	 * {@link FriendshipManager#isFollowing(UserAccount)}
	 */
	public boolean isFollowing(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isFollowing(ua);
	}

	/**
	 * {@link FriendshipManager#block(UserAccount)}
	 */
	public UserAccount block(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).block(ua);
	}
	
	/**
	 * {@link FriendshipManager#unblock(UserAccount)}
	 */
	public UserAccount unblock(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).unblock(ua);
	}
	
	/**
	 * {@link FriendshipManager#isBlocking(UserAccount)}
	 */
	public boolean isBlocking(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isBlocking(ua);
	}
	
	/**
	 * {@link FriendshipManager#getFriendsID(Query)}
	 * @deprecated Use {@link UserAccountManager#getFriendsIDs(Query)}.
	 */
	public String[] getFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFriendsID(query);
	}
	
	/**
	 * {@link FriendshipManager#getFollowersID(Query)}
	 * @deprecated Use {@link UserAccountManager#getFollowersIDs(Query)}.
	 */
	public String[] getFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFollowersID(query);
	}

	/**
	 * {@link FriendshipManager#getFriendsIDs(Query)}
	 */
	public Cursor getFriendsIDs(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFriendsIDs(query);
	}

	/**
	 * {@link FriendshipManager#getFollowersIDs(Query)}
	 */
	public Cursor getFollowersIDs(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFollowersIDs(query);
	}

	/**
	 * {@link FriendshipManager#getIncomingFollowersID(Query)}
	 */
	public String[] getIncomingFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return
			FriendshipManager.getInstance(this).getIncomingFollowersID(query);
	}
	
	/**
	 * {@link FriendshipManager#getOutgoingFriendsID(Query)}
	 */
	public String[] getOutgoingFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getOutgoingFriendsID(query);
	}
	
	/**
	 * {@link ListManager#getLists()}
	 */
	public List[] getLists() throws IOException,
		LimitExceededException {
		checkValid();
		//
		return ListManager.getInstance(this).getLists();
	}
	
	/**
	 * {@link ListManager#getSubscriptions()}
	 */
	public List[] getListSubscriptions() throws IOException,
		LimitExceededException {
		checkValid();
		//
		return ListManager.getInstance(this).getSubscriptions();
	}
	
	/**
	 * <p>
	 * Update account information of authenticated user. Only name, description,
	 * URL and location can be changed.
	 * </p>
	 * @param newUserInfo New user info.
	 * @return New user account info.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If user info is null/empty.
	 * @see MetadataSet#USERACCOUNT_NAME
	 * @see MetadataSet#USERACCOUNT_DESCRIPTION
	 * @see MetadataSet#USERACCOUNT_URL
	 * @see MetadataSet#USERACCOUNT_LOCATION
	 */
	public UserAccount updateProfile(UserAccount newUserInfo)
		throws IOException, LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (newUserInfo == null) {
			throw new IllegalArgumentException(
				"New user info must not be null.");
		}
		//
		HttpRequest req =
			createRequest(
				getURL(TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE));
		req.setMethod(HttpConnection.POST);
		//
		String info = newUserInfo.getString(MetadataSet.USERACCOUNT_NAME);
		if (info != null) {
			if (info.length() > UserAccount.MAX_LEN_NAME) {
				throw new IllegalArgumentException(
					"Name must not be longer than " + 
						UserAccount.MAX_LEN_NAME + " characters.");
			}
			//
			req.setBodyParameter("name", info);
		}
		//
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_DESCRIPTION);
		if (info != null) {
			if (info.length() > UserAccount.MAX_LEN_DESCRIPTION) {
				throw new IllegalArgumentException(
					"Description must not be longer than " +
						UserAccount.MAX_LEN_DESCRIPTION + " characters.");
			}
			//
			req.setBodyParameter("description", info);
		}
		//
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_URL);
		if (info != null) {
			if (info.length() > UserAccount.MAX_LEN_URL) {
				throw new IllegalArgumentException(
					"Url must not be longer than " + 
						UserAccount.MAX_LEN_URL + " characters.");
			}
			//
			req.setBodyParameter("url", info);
		}
		//
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_LOCATION);
		if (info != null) {
			if (info.length() > UserAccount.MAX_LEN_LOCATION) {
				throw new IllegalArgumentException(
					"Location must not be longer than " + 
						UserAccount.MAX_LEN_LOCATION + " characters.");
			}
			//
			req.setBodyParameter("location", info);
		}
		//
		if (req.getBodyParameters().size() == 0) {
			throw new IllegalArgumentException(
				"New user info must not be empty.");
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			UserJSONHandler handler = new UserJSONHandler();
			parser.parse(resp.getStream(), handler);
			//
			handler.loadParsedUserAccount(newUserInfo, 0);
			//
			return newUserInfo;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}
	
	/**
	 * <p>
	 * Report a given user as spammer.
	 * </p>
	 * @param user User.
	 * @return User account object.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount reportSpam(UserAccount user) throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		user.validateUserNameOrID();
		//
		String[] pv = user.getUserNameOrIDParamValue();
		String param = "?" + pv[0] + "=" + pv[1] + "&include_entities=true";
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_REPORT_SPAM) + param);
		req.setMethod(HttpConnection.POST);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			UserJSONHandler handler = new UserJSONHandler();
			parser.parse(resp.getStream(), handler);
			//
			handler.loadParsedUserAccount(user, 0);
			//
			return user;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}

	/**
	 * <p>
	 * Search for users similar to Find People button on Twitter.com. The
	 * results returned by people search on Twitter.com are the same as those 
	 * returned by this API request.
	 * </p>
	 * <pre>
	 * UserAccountManager uam = ...;
	 * if (uam.verifyCredential()) {
	 *   Query query = QueryComposer.query("Twitter API ME");
	 *   UserAccount[] users = uam.search(query);
	 *   ...
	 * }
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#query(String)} <b>(required)</b></li>
	 * <li>{@link QueryComposer#perPage(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param query Search query.
	 * @return Users that match the criteria.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If query null or not properly informed.
	 */
	public UserAccount[] search(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (query == null) {
			throw new IllegalArgumentException("Query must not be null.");
		}
		//
		if (query.toString().indexOf("q=") == -1) {
			throw new IllegalArgumentException(
				"QueryComposer#query(String) is required.");
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_USERS_SEARCH);
		url += "?" + query.toString();
		//
		HttpRequest req = createRequest(url);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			UserJSONHandler handler = new UserJSONHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedUserAccounts();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}
	
	/**
	 * {@link FriendshipManager#isFollowedBy(UserAccount)}
	 */
	public boolean isFollowedBy(UserAccount user) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isFollowedBy(user);
	}
	
	/**
	 * {@link FriendshipManager#isEnabledToSendDMTo(UserAccount)}
	 */
	public boolean isEnabledToSendDMTo(UserAccount user) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isEnabledToSendDMTo(user);
	}
	
	/**
	 * <p>
	 * Return users worth of extended information, specified by either ID, 
	 * screen name, or combination of the two. The author's most recent status 
	 * (if the authenticating user has permission) will be returned inline.
	 * </p>
	 * <pre>
	 * UserAccountManager uam = ...;
	 * if (uam.verifyCredential()) {
	 *   Query query =
	 *       QueryComposer.screenNames(new String[] {"username1", "username2"});
	 *   UserAccount[] users = uam.lookup(query);
	 *   ...
	 * }
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userIDs(String[])} <b>(required)</b> or</li>
	 * <li>{@link QueryComposer#screenNames(String[])} <b>(required)</b></li>
	 * <li>{@link QueryComposer#skipStatus()}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param query Search query.
	 * @return Users associated to the given ids/usernames.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If query null or not properly informed.
	 */
	public UserAccount[] lookup(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (query == null) {
			throw new IllegalArgumentException("Query must not be null.");
		}
		//
		final String qryStr = query.toString();
		//
		if (qryStr.indexOf("user_id=") == -1
				&& qryStr.indexOf("screen_name=") == -1) {
			throw new IllegalArgumentException(
				"QueryComposer#userIDs(String[]) or " + 
					"QueryComposer#screenNames(String[]) is required.");
		}
		//
		HttpRequest req = 
			createRequest(getURL(TWITTER_API_URL_SERVICE_USERS_LOOKUP));
		req.setMethod(HttpConnection.POST);
		//
		Hashtable params = HttpRequest.getQueryStringParams(qryStr);
		Enumeration keys = params.keys();
		//
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			//
			req.setBodyParameter(key, params.get(key).toString());
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			UserJSONHandler handler = new UserJSONHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedUserAccounts();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof UserAccountManager)) {
			return false;
		} else {
			return credential.equals(((UserAccountManager)o).credential);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return credential.hashCode();
	}
	
	/**
	 * <p>
	 * Get the user's credentials.
	 * </p>
	 * @return Credential object.
	 */
	Credential getCredential() {
		return credential;
	}
	
	/**
	 * <p>
	 * Create a HttpRequest object.
	 * </p>
	 * @param url URL.
	 * @param method Http method.
	 * @return Request object.
	 */
	synchronized HttpRequest createRequest(String url) {
		HttpRequest req = new HttpRequest(url);
		req.setSigner(signer, token);
		//
		return req;
	}

	/**
	 * <p>
	 * Check whether the instance is still valid.
	 * </p>
	 * @throws IllegalStateException Instance invalidated.
	 */
	private synchronized void checkValid() {
		if (invalidated) {
			throw new IllegalStateException(
				"This instance is no longer valid. Get a new one!");
		}
	}

	/**
	 * <p>
	 * Check whether it is verified.
	 * </p>
	 * @throws SecurityException If it is not properly verified.
	 */
	private void checkVerified() {
		if (!verified) {
			throw new SecurityException(
				"User's credentials have not been verified yet.");
		}
	}

	/**
	 * <p>
	 * Save the instance on pool.
	 * </p>
	 */
	private synchronized void saveSelfOnPool() {
		if (userAccountMngrPoll.get(credential) == null) {
			userAccountMngrPoll.put(credential, this);
		}
	}
}