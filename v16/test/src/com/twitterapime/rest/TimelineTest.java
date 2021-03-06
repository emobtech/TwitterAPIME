/**
 * 
 */
package com.twitterapime.rest;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author ernandes
 *
 */
public class TimelineTest extends TwitterAPIMETestCase implements SearchDeviceListener {
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
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		try {
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1, true);
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2, true);
			//
			TweetER t1 = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
			t1.post(new Tweet("Test msg 1 @twiterapimetest " + System.currentTimeMillis()));
			t1.post(new Tweet("Test msg 2 @twiterapimetest " + System.currentTimeMillis()));
			t1.send(new Tweet("twiterapimetest", "Test DM 1 " + System.currentTimeMillis()));
			t1.send(new Tweet("twiterapimetest", "Test DM 2 " + System.currentTimeMillis()));
			//
			TweetER t2 = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2));
			t2.post(new Tweet("Test msg 1 " + System.currentTimeMillis()));
			t2.post(new Tweet("Test msg 2 " + System.currentTimeMillis()));
			t2.send(new Tweet("twiterapimetst2", "Test DM 1 " + System.currentTimeMillis()));
			t2.send(new Tweet("twiterapimetst2", "Test DM 2 " + System.currentTimeMillis()));
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() {
		try {
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1, false);
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2, false);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			Timeline.getInstance(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_3_NON_EXISTENT));
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		Timeline t = Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		assertNotNull("test: 5", t);
		assertSame("test: 6", t, Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1)));
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance()}.
	 */
	public void testGetInstance() {
		Timeline t = Timeline.getInstance();
		assertNotNull("test: 1", t);
		assertSame("test: 2", t, Timeline.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetPublicTweets(com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetPublicTweets() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetPublicTweets(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			count = 0;
			//
			t.startGetPublicTweets(this);
			//
			waitFor(10000);
			//
			assertTrue("test: 3", count > 0);
		} catch (Exception e) {
			fail("test: 4");
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetHomeTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetHomeTweets() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetHomeTweets(null, null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.startGetHomeTweets(null, this);
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		t = Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			tweetsFromUserCount = 0;
			tweetsFromFollowerCount = 0;
			//
			t.startGetHomeTweets(null, this);
			//
			waitFor(10000);
			//
			assertTrue("test: 5", tweetsFromUserCount > 0);
			assertTrue("test: 6", tweetsFromFollowerCount > 0);
		} catch (Exception e) {
			fail("test: 7");
		}
		//
		try {
			count = 0;
			//
			t.startGetHomeTweets(QueryComposer.count(1), this);
			//
			waitFor(10000);
			//
			assertEquals("test: 8", 1, count);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetUserTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetUserTweets() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetUserTweets(null, null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.startGetUserTweets(null, this);
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		t = Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			tweetsFromUserCount = 0;
			count = 0;
			//
			t.startGetUserTweets(null, this);
			//
			waitFor(10000);
			//
			assertTrue("test: 5", tweetsFromUserCount > 0);
			assertEquals("test: 6", count, tweetsFromUserCount);
		} catch (Exception e) {
			fail("test: 7");
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
			assertEquals("test: 8", 1, tweetsFromUserCount);
			assertEquals("test: 9", count, tweetsFromUserCount);
		} catch (Exception e) {
			fail("test: 10");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetMentions(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetMentions() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetMentions(null, null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.startGetMentions(null, this);
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		t = Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			tweetsReferencesUserCount = 0;
			count = 0;
			//
			t.startGetMentions(null, this);
			//
			waitFor(10000);
			//
			assertTrue("test: 5", tweetsReferencesUserCount > 0);
			assertEquals("test: 6", count, tweetsReferencesUserCount);
		} catch (Exception e) {
			fail("test: 7");
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
			assertEquals("test: 8", 1, tweetsReferencesUserCount);
			assertEquals("test: 9", count, tweetsReferencesUserCount);
		} catch (Exception e) {
			fail("test: 10");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetDirectMessages(com.twitterapime.search.Query, boolean, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetDirectMessages() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetDirectMessages(null, true, null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.startGetDirectMessages(null, true, this);
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		t = Timeline.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			count = 0;
			tweetsFromRecipientUserCount = 0;
			//
			t.startGetDirectMessages(null, true, this);
			//
			waitFor(10000);
			//
			assertTrue("test: 5", tweetsFromRecipientUserCount > 0);
			assertEquals("test: 6", count, tweetsFromRecipientUserCount);
		} catch (Exception e) {
			fail("test: 7");
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
			assertTrue("test: 8", tweetsFromUserCount > 0);
			assertEquals(count, tweetsFromUserCount);
		} catch (Exception e) {
			fail("test: 9");
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
			assertEquals("test: 10", 1, tweetsFromRecipientUserCount);
			assertEquals("test: 11", count, tweetsFromRecipientUserCount);
		} catch (Exception e) {
			fail("test: 12");
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
			if ("twiterapimetst2".equals(ua.getString(MetadataSet.USERACCOUNT_USER_NAME))) {
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
		fail("test searchFailed: " + cause.getClass().getName());
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
		finished = true;
	}
}