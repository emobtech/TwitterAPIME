/*
 * HttpConnector.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * <p>
 * This is factory class for creating new HttpConnection objects.
 * </p>
 * <p>
 * The creation of HttpConnection is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * <p>
 * The parameter string that describes the target should conform to the Http URL
 * format as described in RFC 1738.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.0
 * @see HttpConnection
 */
public final class HttpConnector {
	/**
	 * <p>
	 * Create and open a HttpConnection.
	 * </p>
	 * 
	 * @param url The URL for the connection.
	 * @return A new HttpConnection object.
	 * @throws IOException If an I/O error occurs.
	 * @throws IllegalArgumentException If url is null or empty.
	 */
	public static HttpConnection open(String url) throws IOException {
		if (url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("URL must not be null/empty.");
		}
		//
		final String JAVA_ME_HTTP_IMPL_CLASS =
			"impl.javame.com.twitterapime.io.HttpConnectionImpl";
		final String JAVA_ME_HTTP_USER_AGENT =
			"Twitter API ME/1.1 (compatible; Java ME; MIDP-2.0; CLDC-1.0)";
		final String ANDROID_HTTP_IMPL_CLASS =
			"impl.android.com.twitterapime.io.HttpConnectionImpl";
		final String ANDROID_HTTP_USER_AGENT =
			"Twitter API ME/1.1 (compatible; Android 1.1)";
		//
		final long PPID = PlatformProviderSelector.getCurrentProvider().getID();
		//
		HttpConnection conn = null;
		String userAgent = null;
		//
		if (PPID == PlatformProvider.PPID_JAVA_ME) {
			conn = newInstance(JAVA_ME_HTTP_IMPL_CLASS);
			userAgent = JAVA_ME_HTTP_USER_AGENT;
		} else if (PPID == PlatformProvider.PPID_ANDROID) {
			conn = newInstance(ANDROID_HTTP_IMPL_CLASS);
			userAgent = ANDROID_HTTP_USER_AGENT;
		} else {
			throw new IllegalArgumentException("Unknown platform ID: " + PPID);
		}
		//
		// #ifdef DEBUG
		System.out.println(url);
		// #endif
		conn.open(url);
		conn.setRequestProperty("User-Agent", userAgent);
		//
		return conn;
	}

	/**
	 * <p>
	 * Create a new instance of a HttpConnection subclass.
	 * </p>
	 * @param className Subclass's full name.
	 * @return New HttpConnection instance.
	 */
	private static HttpConnection newInstance(String className) {
		try {
			return (HttpConnection)Class.forName(className).newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		}
		//
		return null;
	}

	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private HttpConnector() {
	}
}