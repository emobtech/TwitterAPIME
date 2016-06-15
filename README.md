# Twitter API ME

**Twitter API ME** is a compelling and well defined API for Java developers who wish to develop applications that provide any type of access to Twitter's services. The API provides support for the main and more popular services on Twitter, e.g., *tweet* searching and posting. Each provided functionality in turn is very straightforward to use, just like Twitter's philosophy itself.

Another great characteristic of Twitter API ME is the capability of running on different Java platforms. In other words, you can use the API to write applications for Java SE, Java ME, Android, RIM or any other Java compliant platform.

## History

The history of Twitter API ME development is quite simple and short. Everything started on August/2009, during Ernandes' vacation. As he did not have many things to do and was very curious on Twitter, Ernandes decided to exercise his creative leisure designing and implementing an API to access Twitter's services. As an enthusiast of mobile development, he first decided to design an API for Java ME. However, he did not last much to change his mind and then decide to write an API to run on any Java platform.

The first version took about two months to be released, since Ernandes just worked during his free time. On October 29th, the version 1.0 was published only providing the searching *tweets* functionality. As developers started to meet Twitter API ME, Ernandes continued to work on new functionalities, mainly the one for posting *tweets*. Two months later, on Jan 5th, 2010, the version 1.1 was publishing, now supporting *tweet* posting.

## Licensing

Twitter API ME Project is under two licenses: **[GNU General Public License v2.0](http://en.wikipedia.org/wiki/GNU_General_Public_License)** regarding the source code and **[GNU Lesser General Public License v3.0](http://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License)** for the binaries. It means that now you can develop proprietary applications with Twitter API ME if you merely link them to API's binaries.

## Update History

Twitter API ME is now at its tenth release (**1.9**). This last one only brings updates to new Twitter API v1.1, besides some bug fixes.

Version | Date | Contents
------- | ---- | --------
1.9 | 06/15/2011 | 1. Updated to Twitter API v1.1.<br/> 2. Some classes and methods deprecated due API changes.
1.8 | 10/06/2011 | 1. RIM connection string customization.<br/> 2. Improvement on RIM data connection establishment.<br/> 3. Geo-located Trend search based on Yahoo! Where On Earth ID.
1.7 | 06/10/2011 | 1. Reply tweet.<br/> 2. Timeline of favorite tweets.<br/> 3. Favorite/unfavorite tweet.<br/> 4. User search and lookup.<br/> 5. Get friends and followers.<br/> 6. Get friendship details between two users.
1.6 | 02/14/2011 | 1. Retweets to me timeline.<br/> 2. Retweets by me timeline.<br/> 3. Tweets from list timeline.<br/> 4. List management (e.g. create, update, memberships, subscriptions, etc).<br/> 5. Special character (*) in password bug fix.
1.5 | 10/31/2010 | 1. List of pending friends/followers request.<br/> 2. Report spam.<br/> 3. Retweets of me timeline.<br/> 4. Trend Topics search.<br/> 5. Tweet's entities.<br/> 6. Retrieval of access token from UserAccountManager class.
1.4 | 08/29/2010 | 1. Single Access Token auth.<br/> 2. Geo-located tweets.<br/> 3. Friends/Followers list.<br/> 4. Update user profile.
1.3 | 06/14/2010 | 1. xAuth support.<br/> 2. UTF-8 characters support.<br/> 3. Possibility to change service URLs used by API.
1.2 | 04/28/2010 | 1. Tweet repost (*retweet*).<br/> 2. Direct message posting and retrieval.<br/> 3. Friendship management (e.g. follow, block, etc.).<br/> 4. Timelines access.<br/> 5. Rate limit status access for Search API.
1.1 | 01/05/2010 | 1. Tweet post.<br/> 2. User account authentication.<br/> 3. Rate limit status access for REST API.<br/> 4. Android support.<br/> 5. Some bug fixes and optimizations.
1.0 | 10/29/2009 | 1. Tweet search.<br/> 2. Java ME support.

## Functionalities

Twitter API ME attempts to provide support for most functionalities available by [Twitter API](http://dev.twitter.com). See below all available functionalities:

Functionality | Description
------------- | -----------
Tweet search* | Create your own queries and search for all *tweets* that match your criteria.
Tweet post | Submit your *tweets* in a very easy and quick way. Inclusive with geo-location information.
Tweet repost (*retweet*) | Resubmit that interesting *tweet*, so that your friends can also see it.
User account authentication | Check the credentials of a given user account via xAuth and OAuth.
User account data access | Access the profile information (e.g. name, URL, location, etc.) of a given user account.
Update user profile | Update user profile data, i.e., name, description, URL and location.
Rate limit status access* | Check your current rate limit status (Search and REST API) and be aware of how many requests left.
Direct Message sending | Submit your private *tweets* in a very easy and quick way.
Timeline access | Access the main timelines (e.g. *Public**, *Home*, *Mentions*, *Retweets of me*,  etc.) and see what all your friends are texting.
Friendship management | Manage (follow/block) who you follow and your followers. In addition, you can also access your friends/followers list.
Report spam | Report a spammer user to Twitter.
Trend Topics search* | Search for the most popular topics being discussed on Twitter.
List management | Manage your lists, create, update, add/remove members, subscriptions, access timeline, etc.
Reply Tweet | Reply those tweets adding your comments to them.
Favorite/Unfavorite Tweet | Mark those tweets you like as favorite.
User Search | Search for users using part of their names or usernames.
User Lookup | Get the full data about a given user.

*Deprecated due Twitter API v1.1 changes.

## Authentication Methods

Twitter API ME supports all [authentication methods](https://dev.twitter.com/docs/auth) provided by Twitter API. Each style in turn has its own particularity and can be used according to each application's needs and capabilities. See below all the methods:

### 1. Authenticating with xAuth

xAuth is an authentication method supported by Twitter API ME, which is more suitable for mobile applications. This method is much safer than [Basic access authentication](http://en.wikipedia.org/wiki/Basic_access_authentication) used previously, which was deprecated. To work with xAuth is very simple, however, there are some requirements: First, you need to get your [application registered](https://dev.twitter.com/apps/new) to Twitter API, so they can provide to you some access keys (i.e. consumer and secret). Second, you must send an e-mail to [api@twitter.com](mailto:api@twitter.com), requesting xAuth privileges for your app. You can't skip these steps, otherwise you will not be able to authenticate. To know more about this process, access [xAuth](https://dev.twitter.com/docs/oauth/xauth).

Be aware this request may take a few days to be processed. In addition, be also advised to request it just when your app is about to be released. Twitter API has been very restrictive on granting xAuth permission to apps, because they want everybody go **OAuth**.

It is also important to point out that xAuth has now some limitations, due to a [policy change](https://dev.twitter.com/docs/application-permission-model) by Twitter API. For instance, by using xAuth, your app will no longer be able to access Direct Messages. So if you need this type of resource, you must go OAuth.

Twitter API ME's xAuth classes are fully reusable by any other API or app, so you can use it to work with any other xAuth service, besides Twitter. So whether you are looking for just a xAuth lib, Twitter API ME provides it for you. To know more about it, read this [article](http://j2megroup.blogspot.com/2010/10/xauth-lib-oauth-made-easy.html).

### 2. Authenticating with Single Access Token

Twitter API ME also supports **Single Access Token** authentication method, which is ideal for applications with single-user use cases. This method has the same steps mentioned above on xAuth, on the other hand there is no need to request any privilege to Twitter API to start using it, which also turns it into a suitable approach to be used during development phase. However, any request sent to Twitter will be on your behalf. So you will not be able to authenticate with other accounts, but only with yours. For further information, access [Single-user OAuth](http://dev.twitter.com/pages/oauth_single_token).

### 3. Authenticating with OAuth

OAuth is the official authentication method provided by Twitter API and also supported by Twitter API ME. All other methods are derived from OAuth, in order to provide more options for apps according to their needs and capabilities. On the other hand, Twitter recommends that all apps go OAuth, because is safer for users, since there is no way apps get access to their passwords.

To start working with OAuth, the developers just need to get their [apps registered](https://dev.twitter.com/apps/new) on Twitter API, in order to get the app's keys (consumer and secret). In addition, a callback Url must also be informed. This Url is important, because it is used to redirect to the app, as soon as the authentication process is completed. No permission request is necessary.

Since the whole authentication process is performed in a web page provided by Twitter API, your app must be able to support any type of browser integration. At least to open an external browser. Otherwise, it is not possible to work with OAuth. Fortunately, all platforms supported by Twitter API ME provides any type of browser integration. So, you are good go!

To help you get started with OAuth as fast as possible, a sample app was implemented in all supported platforms, demonstrating how to integrate OAuth into your app. Get it from our [download](http://kenai.com/projects/twitterapime/downloads/directory/Miscellaneous) section.

To know more about this process, access [Using OAuth](https://dev.twitter.com/docs/auth/oauth).

### 4. Authenticating with Out-of-band/PIN Code Authentication

For applications that really can't fully integrate with browser, but at least can open an external one, Twitter API provides the out-of-band/PIN code authentication mode, also known as **oob**.

This authentication flow is almost identical to OAuth except instead of being directed back to your website (or app) the user is presented with a PIN code. The user is then asked to type this PIN code into your app which will then complete the authentication process. Since there is redirection, a callback Url is not required.

Twitter API ME also supports oob for all platforms. Except, for Java ME, which it is the only one method to work with OAuth so far, because LDCUI does not provide any browser component. In this case, the browser integration is performed via *MIDlet.platformRequest()* method. So, as soon as the PIN is presented, the user must close the browser and then return to the app to enter the PIN to conclude the authentication.

## Supported Platforms

As advanced in the introduction, a great characteristic of Twitter API ME is the capability of running on different Java platforms. The core components are implemented using POJOs (Plain Old Java Objects) and the most common packages and classes, e.g., *java.lang*, *java.util*, *Vector*, *String*, etc. This way, Twitter API ME can be present on most Java platforms: from the most compact until the most complete.

The current three Java platforms supported by Twitter API ME are:
* Java Micro Edition (MIDP 2.0 / CLDC 1.0) or newer
* Android 1.5 or newer *(also run on Java SE 1.4 or newer)*
* RIM OS 4.6 or newer (Blackberry)

## Sample Codes

In order to help you to quick learn how to work with Twitter API ME, here it goes some sample codes showing how to perform some common tasks.

### 1. Search for Tweets

All samples below are related to *tweet* search functionality. Be aware that [Twitter Search API](https://dev.twitter.com/docs/api/1/get/search) JUST returns popular and/or recent *tweets*. In other words, do NOT expect to retrieve old *tweets*.

* **Search for ''tweets'' that contain the words "Twitter" and "API":**

```java
...
SearchDevice s = SearchDevice.getInstance();
Query q = QueryComposer.containAll("Twitter API");
Tweet[] twts = s.searchTweets(q);
...
```

* **Search for ''tweets'' that contain the words "Java" or "Android":**

```java
...
SearchDevice s = SearchDevice.getInstance();
Query q = QueryComposer.containAny("Java Android");
Tweet[] twts = s.searchTweets(q);
...
```

* **Search for the latest *tweets* from a given user, e.g., [@twapime](http://twitter.com/#!/twapime):**

```java
...
SearchDevice s = SearchDevice.getInstance();
Query q = QueryComposer.from("twapime");
Tweet[] twts = s.searchTweets(q);
...
```

* **Search for the latest *tweets* from [@twapime](http://twitter.com/#!/twapime) and contain the word "Java":**

```java
...
SearchDevice s = SearchDevice.getInstance();
Query q = QueryComposer.from("twapime");
q = QueryComposer.append(QueryComposer.containAll("Java"));
Tweet[] twts = s.searchTweets(q);
...
```

* **Search for the latest five ''tweets'' related to the hashtag (trend) "#business":**

```java
...
SearchDevice s = SearchDevice.getInstance();
Query q = QueryComposer.resultCount(5);
q = QueryComposer.append(QueryComposer.containHastag("business"));
Tweet[] twts = s.searchTweets(q);
...
```

### 2. Authenticate User

All samples below are related to user's authentication process. Twitter API ME supports all authentication methods provided by Twitter API. You can find further information about those methods and their requirements on **Authentication Methods**.

* **Authenticate user with xAuth method:**

```java
...
Credential c = new Credential("user_name", "password", "conKey", "conSecret");
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  System.out.println("User's credentials are OK! You're now logged in!");
} else {
  System.out.println("User name and/or password are invalid!");
}
...
```

Do not forget to request xAuth privileges to your application, by sending an e-mail to [api@twitter.com](mailto:api@twitter.com). Otherwise, *verifyCredential()* will always return *false*. On average, they have taken about a few days to respond.

* **Authenticate user with Single Access Token method:**

```java
...
Token token = new Token("token_access", "token_secret");
Credential c = new Credential("user_name", "conKey", "conSecret", token);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  System.out.println("User's credentials are OK! You're now logged in!");
} else {
  System.out.println("Consumer or token keys are invalid!");
}
...
```

You can get your "token_access" and "token_secret" on the same page as you get your app's "consumer key" and "consumer secret". Be aware that ALL requests to Twitter using this method will be on your behalf. In other words, you can log in with your user account only.

* **Authenticate user with OAuth method:**

```java
...
//Snippet for Android.
WebView webView = ...;
WebViewOAuthDialogWrapper page = new WebViewOAuthDialogWrapper(webView);

//Snippet for RIM.
BrowserContentManager browserManager = ...;
BrowserContentManagerOAuthDialogWrapper page =
    new BrowserContentManagerOAuthDialogWrapper(browserManager);

//Snippet for Java ME.
MIDlet midlet = ...;
MIDletOAuthDialogWrapper page = new MIDletOAuthDialogWrapper(midlet);

//Snippet common for all platforms.
page.setConsumerKey("conKey");
page.setConsumerSecret("conSecret");
page.setCallbackUrl("callback url"); //For Java ME callback url is not required.
page.setOAuthListener(new OAuthDialogListener() {
    /**Callback when user authorizes the app to access the account.*/
    public void onAuthorize(Token token) {
        Credential c = new Credential("conKey", "conSecret", token);
        UserAccountManager m = UserAccountManager.getInstance();
        if (m.verifyCredential()) {
            //user authorized!
        }
    }
    /**Callback when user denies the app to access the account.*/
    public void onAccessDenied(String message) {}
    /**Callback when any error happens during authentication.*/
    public void onFail(String message, String description) {}
});

page.login(); //runs asynchronously.
...
```

To help you get started with OAuth as fast as possible, a sample app was implemented in all supported platforms, demonstrating how to integrate OAuth into your app. Get it from our [download](http://kenai.com/projects/twitterapime/downloads/directory/Miscellaneous) section.

### 3. Retrieve Rate Limit Status

All codes below are related to Twitter's rate limiting functionality, which you can keep tracking of current values via Twitter API ME. Twitter implements rate limits for both Search and REST API, in order to avoid attacks. For further information on rate limiting, access [Twitter API - Rate Limiting](http://dev.twitter.com/pages/rate-limiting).

In order to keep tracking of current status, Twitter API ME provides means to access both APIs status. For that, you will have to work with the following classes: *SearchDevice*, *UserAccountManager* and *RateLimitStatus*.

*SearchDevice* provides methods to access the status from Search API. On the other hand, *UserAccountManager* accesses the data related to REST API. *RateLimitStatus* in turn just wraps a set of values related to rate limiting status.

* **Check Search API status:**

```java
...
SearchDevice sd = SearchDevice.getInstance();
RateLimitStatus rls = sd.getRateLimitStatus();

System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_HOURLY_LIMIT));
System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_RESET_TIME));
System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_REMAINING_HITS));
...
```

* **Check REST API status:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  RateLimitStatus rls = m.getRateLimitStatus();

  System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_HOURLY_LIMIT));
  System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_RESET_TIME));
  System.out.println(rls.getString(MetadataSet.RATELIMITSTATUS_REMAINING_HITS));
}
...
```

It is important to point out that, Search API limit considers the requests from a given IP address. On other hand, REST API considers the requesting user. That's why REST API requests the user to be authenticated. In additional, in case your code is throwing *LimitExceededException*, be aware you are being rated by Twitter API, because you hit your limit.

### 4. Post Tweets

All codes below are related to Twitter's main functionality: posting *tweets*. Twitter API ME provides a very straightforward way to work with this functionality. Which could not be different, since how easy and practical this functionality is. To post a *tweet*, the user authentication is required.

In order to post a *tweet*, you will have to work with the following classes: *Tweet* and *TweetER*.

*Tweet* is the class that basically wraps the *tweet*`s info (i.e. user name, text, source, etc) and *TweetER* is responsible for managing the posting process with Twitter API.

* **Post a new *tweet*:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Tweet t = new Tweet("Hi!!! This is my first tweet via Twitter API ME. \o/");
  TweetER ter = TweetER.getInstance(m);

  t = ter.post(t);
}
...
```

The returned *tweet* is same instance as passed as parameter, however it now contains some more info provide by Twitter API.

* **Post a geo-located *tweet*:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  GeoLocation loc = new GeoLocation("+37.5", "+26.7");
  Tweet t = new Tweet("Cool! Geo-located tweet via Twitter API ME. \o/", loc);
  TweetER ter = TweetER.getInstance(m);

  t = ter.post(t);
}
...
```

Important to point out that in order to be able to post geo-located *tweets*, first off you need to enable "Tweet Location" setting in your user account, on Twitter website. Otherwise, you will not see any location data in your *tweets*.

* **Repost a *tweet* (*retweet*):**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  TweetER ter = TweetER.getInstance(m);
  Tweet t = ter.findByID("5698745621");

  t = ter.repost(t);
}
...
```

The *TweetER.repost(Tweet)* method just requires that *tweet*`s ID be informed to perform the operation.

### 5. Send Direct Messages

All codes below are related to Twitter's functionality of sending Direct Messages. A DM, as it is popularly known, is just like a *tweet*, but it is kept private between sender and recipient. Just like posting a *tweet*, Twitter API ME provides a very straightforward way to work with this functionality. To send a DM, the user authentication is also required.

In order to send a DM, you will work with the same classes as posting a *tweet*.

* **Send a Direct Message:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Tweet t = new Tweet("recipient_user_name", "Hi!!! Now I can send DM.");
  TweetER ter = TweetER.getInstance(m);

  t = ter.send(t);
}
...
```

The returned *tweet* is same instance as passed as parameter, however it now contains some more info provide by Twitter API.

### 6. Friendship Management

All codes below are related to Twitter's friendship management functionalities. Follow people and be followed are so important as posting *tweets*. Otherwise, there would not be reason to *tweet*, right? Twitter API ME provides means so you can manage the people you follow and your followers. Even being easy to manage your friends, your authentication is also required for this.

To manage your friends, you will only work with the *UserAccount* and *UserAccountManager/FriendshipManager* classes.

* **Follow a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  ua = m.follow(ua);
}
...
```

* **Unfollow a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  ua = m.unfollow(ua);
}
...
```

The returned *UserAccount* object is same instance as passed as parameter, however it now contains some more info provide by Twitter API.

* **Check whether you are following a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  if(m.isFollowing(ua)) {
    System.out.println("I am already following this friend.");
  }
}
...
```

* **Block a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  ua = m.block(ua);
}
```
</pre>

* **Unblock a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  ua = m.unblock(ua);
}
...
```

* **Check whether you are blocking a friend:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount ua = new UserAccount("friend_user_name");

  if(m.isBlocking(ua)) {
    System.out.println("I am already blocking this friend.");
  }
}
...
```

* **Get your friends ID list:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  FriendshipManager fmngr = FriendshipManager.getInstance(m);
  String[] ids = fmngr.getFriendsID(null); //get all friends ID.
  ...
  ids = fmngr.getFriendsID(QueryComposer.count(5)); //get up to 5 friends ID.
}
...
```

Be aware this method can take too long to return and consume a lot of memory, in case you have a high number of friends. In this case, we suggest you limit the number of IDs to be returned.

* **Get your followers ID list:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  FriendshipManager fmngr = FriendshipManager.getInstance(m);
  String[] ids = fmngr.getFollowersID(null); //get all followers ID.
  ...
  ids = fmngr.getFollowersID(QueryComposer.count(5)); //get up to 5 followers ID.
  ...
}
...
```

Once you get your friends/followers ID, you can use the method *UserAccountManager.getUserAccount(UserAccount("user_id"))* to fetch the full profile of a given user by his/her ID.

* **Get your pending follower request list:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  FriendshipManager fmngr = FriendshipManager.getInstance(m);
  String[] ids = fmngr.getIncomingFollowersID(null); //get all pending requests.
  ...
}
...
```

* **Get your pending friend request list:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  FriendshipManager fmngr = FriendshipManager.getInstance(m);
  String[] ids = fmngr.getOutgoingFriendsID(null); //get all pending requests.
  ...
}
...
```

### 7. Get Timelines

All codes below are related to Twitter's timelines. A timeline is a list of *tweets* chronologically ordered. The most famous timeline is the one that is shown as soon as you access your Twitter's home, which shows all *tweets* from people you follow. Besides *Home* timeline, Twitter API ME still provides access to others timelines, i.e., *Public*, *User*, *Mentions* and *Direct Messages*. Except *Public* one, the others require user authentication.

To access the timelines, you will basically work with *Timeline* class and *SearchDeviceListener* interface.

*Timeline* is responsible for managing with Twitter API the access to timelines' data. *SearchDeviceListener* is a interface that you must implement to listen to the events triggered by *Timeline*, e.g., found *tweets*.

* **Get Public timeline:**

```java
...
Timeline tml = Timeline.getInstance();

tml.startGetPublicTweets(new SearchDeviceListener() {
  ...
  public void tweetFound(Tweet tweet) {
    System.out.println("Tweet received:\n" + tweet);
  }
  ...
};
...
```

* **Get tweets from Home timeline:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);

  tml.startGetHomeTweets(null, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("Tweet received:\n" + tweet);
    }
    ...
  };
}
...
```

* **Get latest five tweets from Home timeline:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);
  Query q = QueryComposer.count(5);

  tml.startGetHomeTweets(q, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("Tweet received:\n" + tweet);
    }
    ...
  };
}
...
```

* **Get tweets from User timeline:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);

  tml.startGetUserTweets(null, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("Tweet received:\n" + tweet);
    }
    ...
  };
}
...
```

* **Get tweets from Mentions timeline:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);

  tml.startGetMentions(null, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("Tweet received:\n" + tweet);
    }
    ...
  };
}
...
```

* **Get received tweets from Direct Message timeline:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);

  tml.startGetDirectMessages(null, true, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("DM received:\n" + tweet);
    }
    ...
  };
}
...
```

* **Get your ''tweets'' that were ''retweeted'':**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Timeline tml = Timeline.getInstance(m);

  tml.startGetRetweetsOfMe(null, new SearchDeviceListener() {
    ...
    public void tweetFound(Tweet tweet) {
      System.out.println("Tweet received:\n" + tweet);
    }
    ...
  };
}
...
```

Besides *SearchDeviceListener.tweetFound(Tweet)*, you can also identify when the inquiry is concluded (*searchCompleted()*) or failed (*searchFailed(Throwable)*).

### 8. Update User Profile

All codes below are related to update user profile functionality. Twitter API ME provides a very easy way to update your data. The data you can change via API are name, description, url and location.

In order to update your profile, you will have to work with the following classes: *UserAccount* and *UserAccountManager*.

* **Update all profile data:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  Hashtable d = new Hashtable(4);
  d.put(MetadataSet.USERACCOUNT_NAME, "John Smith");
  d.put(MetadataSet.USERACCOUNT_DESCRIPTION, "I am John and I like...");
  d.put(MetadataSet.USERACCOUNT_URL, "http://www.johnsmith.com");
  d.put(MetadataSet.USERACCOUNT_LOCATION, "San Francisco, CA");

  UserAccount newInfo = new UserAccount(d);

  newInfo = m.updateProfile(newInfo);
}
...
```

If you intend to change only one of those data, for instance your name, just provide it. Twitter API ME only sends the available data to be changed. In addition, the object returned by *updateProfile()* method contains all data about the user, including new info.

### 9. Report Spam

All codes below are related to report spam functionality. Twitter API ME provides a very easy way to report those disturbing spammer users.

In order to report a spammer, you will have to work with the following classes: *UserAccount* and *UserAccountManager*.

* **Report a Spammer:**

```java
...
Credential c = new Credential(...);
UserAccountManager m = UserAccountManager.getInstance(c);

if (m.verifyCredential()) {
  UserAccount spammer = new UserAccount("user_name");
  spammer = m.reportSpam(spammer);
  ...
}
...
```

The *UserAccount* object returned by *reportSpam()* contains the full data about the spammer user.

### 10. Search for Trend Topics

All codes below are related to *Trend Topic* searching functionality. A Trend Topic consists of a given subject being discussed on Twitter. To perform a search, you will basically need to work with two classes: *TrendTopics* and *Topic*.

Topic is the class that basically wraps the trend topic's info (i.e. text, query and date) and TrendTopics is responsible for managing the searching process with Twitter API.

* **Search for trend topics being discussed now:**

```java
...
TrendTopics tts = TrendTopics.getInstance();
Topic[] topics = tts.searchNowTopics(null);
...
```

* **Search for trend topics being discussed along the day:**

```java
...
TrendTopics tts = TrendTopics.getInstance();
Topic[] topics = tts.searchDailyTopics(null);
...
```

* **Search for trend topics being discussed along the week:**

```java
...
TrendTopics tts = TrendTopics.getInstance();
Topic[] topics = tts.searchWeeklyTopics(null);
...
```

Daily and weekly searches support a date filter, where you will get only the trend topics from a given date. For that, use *QueryComposer.date(Date)* method to produce the query.

### 11. Tweet Entities

All codes below are related to *Tweet Entities* retrieval functionality. A *tweet's* entity is a piece of information contained in *tweet's* text, that may be interesting for developers, e.g., urls, user mentions and hashtags. For further information on Tweet Entities, access [Tweet Entities](http://dev.twitter.com/pages/tweet_entities).

Tweet Entity data is available in most functionalities of Twitter API ME that involves *Tweet* class, i.e., posting, reposting, timelines and find. It is important to point out, that Search API does **not** return tweet entities, so you will not get any entity from *tweets* returned by *SearchDevice* class. For timelines, you need to specify in query parameter, that you the entities to be returned. In this case, just use the method *QueryComposer.includeEntities()*. It is necessary, since entities increase the volume of data transmitted from Twitter.

To work with Tweet Entities, you will basically need to work with *TweetEntity* class.

* **Get URLs from a *tweet's* enties:**

```java
...
TweetEntity entity = tweet.getEntity();

if (entity != null) {
  TweetEntity[] urls = entity.getURLs();

  for (int i = 0; i < urls.length; i++) {
    System.out.println(urls[i].getString(MetadataSet.TWEETENTITY_URL));
  }
}
...
```

* **Get hashtags from a *tweet's* entities:**

```java
...
TweetEntity entity = tweet.getEntity();

if (entity != null) {
  TweetEntity[] hashtags = entity.getHashtags();

  for (int i = 0; i < hashtags.length; i++) {
    System.out.println(hashtags[i].getString(MetadataSet.TWEETENTITY_HASHTAG));
  }
}
...
```

* **Get user mentions from a *tweet's* entities:**

```java
...
TweetEntity entity = tweet.getEntity();

if (entity != null) {
  TweetEntity[] mentions = entity.getMentions();

  for (int i = 0; i < mentions.length; i++) {
    System.out.println(
      mentions[i].getString(MetadataSet.TWEETENTITY_USERACCOUNT_ID));
    System.out.println(
      mentions[i].getString(MetadataSet.TWEETENTITY_USERACCOUNT_NAME));
    System.out.println(
      mentions[i].getString(MetadataSet.TWEETENTITY_USERACCOUNT_USER_NAME));
  }
}
...
```

If you are missing an important scenario that could also be presented here, please, [let us know](mailto:support@twitterapime.com).

## Donation

In case of Twitter API ME has brought good benefits for you and/or your company, and because of that you would like to thank us for all our hard work. Please, feel free to donate us any amount, via PayPal, by clicking **[here](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ernandes%40gmail%2ecom&lc=US&item_name=Twitter%20API%20ME%20Team&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHostedGuest)**. It is easy and quick to do. In addition, this contribution will provide us more resources to keep up improving this API for you.

## Apps Powered by Twitter API ME

* [TwAPIme for Android](https://market.android.com/details?id=com.twapime.app)
* [Sountracker](http://www.soundtracker.fm/)
* [EcoTweet](http://www.ecotweet.com/)
* [Meet Me For Dinner](http://java.net/projects/meet-me-sample)
* [ZonaSnap](http://www.mobihand.com/product.asp?id=465276)
* [TwStreet](https://market.android.com/details?id=com.emobtech.twstreet)
* [Beer Buddy for BBM](https://market.android.com/details?id=com.mvt.twitter)
* [Real Twiends](https://market.android.com/details?id=com.twapime.app)
* [DeuterIDE](http://www.deuteride.com/)

## See Also

* [Twitter API (Libraries)](http://dev.twitter.com/pages/libraries)

## References

* [Twitter API](http://dev.twitter.com/)
* [OAuth](http://en.wikipedia.org/wiki/OAuth)
* [Basic access authentication](http://en.wikipedia.org/wiki/Basic_access_authentication)
* [Java Micro Edition](http://java.sun.com/javame/index.jsp)
* [Android](http://developer.android.com)
* [kXML 2](http://kxml.sourceforge.net/kxml2)
* [SAX](http://www.saxproject.org)

## External Links

* [Twitter Official Website](http://www.twitter.com)
* [Follow Twitter API ME](http://www.twitter.com/twapime)
* [J2ME Group Blog](http://j2megroup.blogspot.com)
