/**
 * 
 */
package com.twitterapime.util;

import junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class UtilTestSuite extends TestSuite {
	/**
	 * 
	 */
	public UtilTestSuite() {
		addTest(new StringUtilTest());
		addTest(new QSortTest());
	}
}