/*
 * HttpResponseCodeErrorJSONHandler.java
 * 02/22/2013
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io.handler.json;

import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.parser.ParserException;

/**
 * <p>
 * Handler class for parsing the JSON error messages from Twitter API regarding
 * rate limit error.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.9
 */
public final class HttpResponseCodeErrorJSONHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * Error code.
	 * </p>
	 */
	private int code = -1;
	
	/**
	 * <p>
	 * Error message.
	 * </p>
	 */
	private String message;

	/**
	 * <p>
	 * Create an instance of RateLimitJSONHandler class.
	 * </p>
	 */
	public HttpResponseCodeErrorJSONHandler() {
		super("root");
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		jsonObj = jsonObj.getJSONObject(startKey);
		//
		if (jsonObj.has("errors")) {
			JSONArray errors = jsonObj.getJSONArray("errors");
			//
			if (errors.length() > 0) {
				jsonObj = errors.getJSONObject(0);
				//
				if (jsonObj.has("code")) {
					code = jsonObj.getInt("code");
				}
				if (jsonObj.has("message")) {
					message = jsonObj.getString("message");
				}
			}
		}
	}

	/**
	 * <p>
	 * Return the parsed error code.
	 * </p>
	 * @return Message.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * <p>
	 * Return the parsed error message.
	 * </p>
	 * @return Message.
	 */
	public String getMessage() {
		return message;
	}
}
