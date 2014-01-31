package jt;

import twitter4j.User;

import java.util.Date;

public class ActivityEvent
{
	public final Date time;
	public final Long user;
	public final EventType type;
	public final String text;

	public ActivityEvent(User user, EventType type, String text)
	{
		time = new Date();
		TweetDatabase.putUser(user);
		this.user = user.getId();
		this.type = type;
		this.text = text;
	}

	public User getUser()
	{
		return TweetDatabase.getUser(user);
	}
}
