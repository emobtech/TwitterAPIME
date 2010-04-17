/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

/**
 * @author ernandes
 *
 */
public class TimelineTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private int count;
	
	/**
	 * 
	 */
	private boolean finished;
	
	/**
	 * 
	 */
	private int tweetsFromUserCount;
	
	/**
	 * 
	 */
	private int tweetsFromFollowerCount;
	
	/**
	 * 
	 */
	private int tweetsReferencesUserCount;
	
	/**
	 * 
	 */
	private int tweetsFromRecipientUserCount;

	/**
	 * @param name
	 */
	public TimelineTest() {
		super("TimelineTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			Timeline.getInstance(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			Timeline.getInstance(uam);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance(uam);
		assertNotNull(t);
		assertSame(t, Timeline.getInstance(uam));
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance()}.
	 */
	public void testGetInstance() {
		Timeline t = Timeline.getInstance();
		assertNotNull(t);
		assertSame(t, Timeline.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetPublicTweets(com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetPublicTweets() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetPublicTweets(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			count = 0;
			//
			t.startGetPublicTweets(this);
			//
			waitFor(10000);
			//
			assertTrue(count > 0);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetHomeTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetHomeTweets() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetHomeTweets(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetHomeTweets(null, this);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			tweetsFromUserCount = 0;
			tweetsFromFollowerCount = 0;
			//
			t.startGetHomeTweets(null, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFromUserCount > 0);
			assertTrue(tweetsFromFollowerCount > 0);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			count = 0;
			//
			t.startGetHomeTweets(QueryComposer.count(1), this);
			//
			waitFor(10000);
			//
			assertEquals(1, count);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetUserTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetUserTweets() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetUserTweets(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetUserTweets(null, this);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			tweetsFromUserCount = 0;
			count = 0;
			//
			t.startGetUserTweets(null, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFromUserCount > 0);
			assertEquals(count, tweetsFromUserCount);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			count = 0;
			tweetsFromUserCount = 0;
			//
			t.startGetUserTweets(QueryComposer.count(1), this);
			//
			waitFor(10000);
			//
			assertEquals(1, tweetsFromUserCount);
			assertEquals(count, tweetsFromUserCount);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetMentions(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetMentions() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetMentions(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetMentions(null, this);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			tweetsReferencesUserCount = 0;
			count = 0;
			//
			t.startGetMentions(null, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsReferencesUserCount > 0);
			assertEquals(count, tweetsReferencesUserCount);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			tweetsReferencesUserCount = 0;
			count = 0;
			//
			t.startGetMentions(QueryComposer.count(1), this);
			//
			waitFor(10000);
			//
			assertEquals(1, tweetsReferencesUserCount);
			assertEquals(count, tweetsReferencesUserCount);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetDirectMessages(com.twitterapime.search.Query, boolean, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetDirectMessages() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetDirectMessages(null, true, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetDirectMessages(null, true, this);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			count = 0;
			tweetsFromRecipientUserCount = 0;
			//
			t.startGetDirectMessages(null, true, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFromRecipientUserCount > 0);
			assertEquals(count, tweetsFromRecipientUserCount);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			count = 0;
			tweetsFromUserCount = 0;
			//
			t.startGetDirectMessages(null, false, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFromUserCount > 0);
			assertEquals(count, tweetsFromUserCount);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			count = 0;
			tweetsFromRecipientUserCount = 0;
			//
			t.startGetDirectMessages(QueryComposer.count(1), true, this);
			//
			waitFor(10000);
			//
			assertEquals(1, tweetsFromRecipientUserCount);
			assertEquals(count, tweetsFromRecipientUserCount);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * @param maxTime
	 */
	public void waitFor(long maxTime) {
		finished = false;
		//
		int st = (int)(maxTime / 1000);
		//
		while (st > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			st--;
			//
			if (finished) {
				break;
			}
		}
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	public void tweetFound(Tweet tweet) {
		assertNotNull(tweet);
		count++;
		//
		try {
			UserAccount ua = tweet.getUserAccount();
			if ("twiterapime".equals(ua.getString(MetadataSet.USERACCOUNT_USER_NAME))) {
				tweetsFromFollowerCount++;
			} else {
				if ("twiterapimetest".equals(ua.getString(MetadataSet.USERACCOUNT_USER_NAME))) {
					tweetsFromUserCount++;
				}
			}
			//
			if (tweet.getString(MetadataSet.TWEET_CONTENT).indexOf("@twiterapimetest") != -1) {
				tweetsReferencesUserCount++;
			}
			//
			ua = tweet.getRecipientAccount();
			if (ua != null && "twiterapimetest".equals(ua.getString(MetadataSet.USERACCOUNT_USER_NAME))) {
				tweetsFromRecipientUserCount++;
			}
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	public void searchFailed(Throwable cause) {
		System.out.println(cause);
		fail();
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
		finished = true;
	}
}