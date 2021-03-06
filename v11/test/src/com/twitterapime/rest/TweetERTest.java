/**
 * 
 */
package com.twitterapime.rest;

import java.io.IOException;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;

/**
 * @author Main
 *
 */
public class TweetERTest extends TestCase {
	/**
	 * 
	 */
	public TweetERTest() {
		super("TweetERTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			TweetER.getInstance(null);
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
			TweetER.getInstance(uam);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			uam.verifyCredential();
		} catch (IOException e) {
			fail();
		}
		//
		TweetER t = TweetER.getInstance(uam);
		assertNotNull(t);
		assertSame(t, TweetER.getInstance(uam));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance()}.
	 */
	public void testGetInstance() {
		TweetER t = TweetER.getInstance();
		assertNotNull(t);
		assertSame(t, TweetER.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#findByID(java.lang.String)}.
	 */
	public void testFindByID() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.findByID(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.findByID("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.findByID("7141196879");
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			assertNull(t.findByID("9890asdh9809"));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Tweet tw = t.findByID("7041609437");
			assertNotNull(tw);
			assertEquals("7041609437", tw.getString(MetadataSet.TWEET_ID));
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#post(com.twitterapime.search.Tweet)}.
	 */
	public void testPost() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.post(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.post(new Tweet());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.post(new Tweet("Hello, Twitters!!!"));
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			uam.verifyCredential();
			//
			t = TweetER.getInstance(uam);
			//
			String msg = "Test at " + System.currentTimeMillis() + " milis.";
			Tweet tw1 = new Tweet(msg);
			Tweet tw2 = t.post(tw1);
			//
			assertSame(tw1, tw2);
			assertEquals(msg, tw2.getString(MetadataSet.TWEET_CONTENT));
		} catch (IOException e) {
			fail();
		}
	}
}