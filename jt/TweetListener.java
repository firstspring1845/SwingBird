package jt;

import twitter4j.Status;

public interface TweetListener
{
	public void onStatus(Status status);
}
