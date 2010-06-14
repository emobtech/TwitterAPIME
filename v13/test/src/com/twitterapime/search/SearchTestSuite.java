/**
 * 
 */
package com.twitterapime.search;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class SearchTestSuite extends TestSuite {
	/**
	 * 
	 */
	public SearchTestSuite() {
		addTest(new QueryComposerTest());
		addTest(new QueryTest());
		addTest(new SearchDeviceTest());
		addTest(new TweetTest());
	}
}