/*
 * HttpResponseCodeInterpreter.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;

import com.twitterapime.io.handler.HttpResponseCodeErrorHandler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;

/**
 * <p>
 * This class is responsible for interpreting the Http response-codes from
 * Twitter API, in order to check the status of a request: success or failure.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.1
 * @see LimitExceededException
 * @see InvalidQueryException
 */
public final class HttpResponseCodeInterpreter {
	/**
	 * <p>
	 * 420: Custom Http code from Twitter API which means to wait a little bit
	 * for a new request.
	 * </p> 
	 */
	public static final int CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM = 420;
	
	/**
	 * <p>
	 * Perform an analyze of a given HttpConnection object's response-code in
	 * order to interpret whether the requests to Twitter API went well.
	 * Otherwise, an exception is thrown describing the problem.
	 * </p>
	 * @param conn HttpConnection object to be interpreted.
	 * @throws IOException If an I/O or service error occurs.
	 * @throws LimitExceededException If a request limit exceeded error occurs.
	 * @throws InvalidQueryException If an invalid query error occurs.
	 * @throws SecurityException If a security error occurs.
	 * @throws IllegalArgumentException If conn is null.
	 */
	public static void perform(HttpConnection conn) throws IOException,
		LimitExceededException {
		if (conn == null) {
			throw new IllegalArgumentException("Connection must not be null.");
		}
		//
		final int respCode = conn.getResponseCode();
		//
		if (respCode != HttpConnection.HTTP_OK
				&& respCode != HttpConnection.HTTP_NOT_MODIFIED) {
			if (isInvalidQueryError(respCode)) {
				throw new InvalidQueryException(getErrorMessage(conn));
			} else if (isLimitExceededError(respCode)) {
				throw new LimitExceededException(getErrorMessage(conn));
			} else if (isSecurityError(respCode)) {
				throw new SecurityException(getErrorMessage(conn));
			} else {
				throw new IOException(getErrorMessage(conn));
			}
		}
	}
	
	/**
	 * <p>
	 * Get error messages from connection's response.
	 * </p>
	 * @param conn Http connection.
	 * @return Message.
	 * @throws IOException If any I/O error occurs.
	 */
	public static String getErrorMessage(HttpConnection conn)
		throws IOException {
		String errorMsg = null;
		//
		Parser parser = ParserFactory.getDefaultParser();
		HttpResponseCodeErrorHandler handler =
			new HttpResponseCodeErrorHandler();
		//
		try {
			parser.parse(conn.openInputStream(), handler);
			//
			errorMsg = handler.getParsedErrorMessage();
		} catch (ParserException e) {
			errorMsg = "HTTP ERROR CODE: " + conn.getResponseCode();
		}
		//
		return errorMsg;
	}

	/**
	 * <p>
	 * Check if the response-code reports a request limit exceeded error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents a limit exceeded error.
	 */
	static boolean isLimitExceededError(int code) {
		return code == HttpConnection.HTTP_BAD_REQUEST
				|| code == HttpConnection.HTTP_FORBIDDEN
				|| code == CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM;
	}
	
	/**
	 * <p>
	 * Check if the response-code reports an invalid query error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents an invalid query error.
	 */
	static boolean isInvalidQueryError(int code) {
		return code == HttpConnection.HTTP_NOT_FOUND
				|| code == HttpConnection.HTTP_NOT_ACCEPTABLE;
	}
	
	/**
	 * <p>
	 * Check if the response-code reports a service error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents a service error.
	 */
	static boolean isServiceError(int code) {
		return code == HttpConnection.HTTP_BAD_GATEWAY
				|| code == HttpConnection.HTTP_INTERNAL_ERROR
				|| code == HttpConnection.HTTP_UNAVAILABLE;
	}
	
	/**
	 * <p>
	 * Check if the response-code reports a security error, e.g., Authentication
	 * credentials were missing or incorrect.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents a security error.
	 */
	static boolean isSecurityError(int code) {
		return code == HttpConnection.HTTP_UNAUTHORIZED;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private HttpResponseCodeInterpreter() {
	}
}