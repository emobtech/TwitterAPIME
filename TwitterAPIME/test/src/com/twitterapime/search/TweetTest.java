/**
 * 
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class TweetTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public TweetTest() {
		super("TweetTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.Tweet#Tweet(java.lang.String)}.
	 */
	public void testTweetString() {
		try {
			new Tweet((String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("aaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBB");
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("aaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBX");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Tweet t = new Tweet("Hello, Twitter API ME!!!");
		assertEquals("Hello, Twitter API ME!!!", t.getString(MetadataSet.TWEET_CONTENT));
	}

	/**
	 * Test method for {@link com.twitterapime.search.Tweet#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		UserAccount ua = new UserAccount();
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_USER_ACCOUNT, ua);
		Tweet t = new Tweet(sample);
		assertSame(ua, t.getUserAccount());
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#Tweet(String, String)}.
	 */
	public void testTweetStringString() {
		try {
			new Tweet((String)null, (String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("username", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("", "text");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Tweet t = new Tweet("username", "text");
			//
			assertEquals("username", t.getString(MetadataSet.TWEET_AUTHOR_USERNAME));
			assertEquals("username", t.getUserAccount().getString(MetadataSet.USERACCOUNT_ID));
			assertEquals("username", t.getUserAccount().getString(MetadataSet.USERACCOUNT_USER_NAME));
			assertEquals("text", t.getString(MetadataSet.TWEET_CONTENT));
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#getRecipientAccount()}.
	 */
	public void testGetRecipientAccount() {
		UserAccount ua = new UserAccount();
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_RECIPIENT_ACCOUNT, ua);
		Tweet t = new Tweet(sample);
		assertSame(ua, t.getRecipientAccount());
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#getRepostedTweet()}.
	 */
	public void testGetRepostedTweet() {
		Tweet ua = new Tweet();
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_REPOSTED_TWEET, ua);
		Tweet t = new Tweet(sample);
		assertSame(ua, t.getRepostedTweet());
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#Tweet(String, com.twitterapime.rest.GeoLocation)}.
	 */
	public void testTweetStringLocation() {
		GeoLocation loc = new GeoLocation();
		//
		assertSame(new Tweet("content", loc).getObject(MetadataSet.TWEET_LOCATION), loc);
		assertSame(new Tweet("content", loc).getLocation(), loc);
		assertNull(new Tweet("content", (GeoLocation)null).getObject(MetadataSet.TWEET_LOCATION));
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#validateContent()}.
	 */
	public void testValidateContent() {
		try {
			new Tweet().validateContent();
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.Tweet#validateRecipient()}.
	 */
	public void testValidateRecipient() {
		try {
			new Tweet().validateRecipient();
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Hashtable t = new Hashtable();
			t.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount());
			//
			new Tweet(t).validateRecipient();
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Hashtable t = new Hashtable();
			t.put(MetadataSet.USERACCOUNT_ID, "123456790");
			t.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(t));
			//
			new Tweet(t).validateRecipient();
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Hashtable t = new Hashtable();
			t.put(MetadataSet.USERACCOUNT_USER_NAME, "twapime");
			t.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(t));
			//
			new Tweet(t).validateRecipient();
		} catch (Exception e) {
			fail();
		}
	}
}