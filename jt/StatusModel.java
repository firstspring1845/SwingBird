package jt;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

public class StatusModel
{

	private Long user;
	private Long id;
	private Long originalId = -1L;

	private String text;
	private String via;

	private boolean fav;

	public StatusModel(Status org)
	{
		Status s = org.isRetweet() ? org.getRetweetedStatus() : org;
		id = s.getId();
		if (org.isRetweet())
		{
			originalId = s.getId();
		}
		TweetDatabase.putUser(org.getUser());
		TweetDatabase.putUser(s.getUser());
		text = s.getText();
		for (URLEntity e : s.getURLEntities())
		{
			text = text.replace(e.getURL(), e.getExpandedURL());
		}
		for (MediaEntity e : s.getMediaEntities())
		{
			text = text.replace(e.getURL(), e.getExpandedURL());
		}
		user = s.getUser().getId();
		via = (org.isRetweet() ? "(RT:" + org.getUser().getScreenName() + ")" : "") + s.getCreatedAt().toString() + " via " + s.getSource().replaceAll("<.+?>", "");
		fav = s.isFavorited();
	}

	public String getName()
	{
		User u = TweetDatabase.getUser(user);
		return u.getScreenName() + " / " + u.getName();
	}

	public String getText()
	{
		return text;
	}

	public String getSource()
	{
		return via;
	}

	public User getUser()
	{
		return TweetDatabase.getUser(user);
	}

	public String getIconURL()
	{
		return TweetDatabase.getUser(user).getProfileImageURL();
	}

	public boolean isFavorited()
	{
		if (originalId != -1)
		{
			return TweetDatabase.getStatus(originalId).isFavorited();
		}
		return fav;
	}

	public void setFavorite(boolean fav)
	{
		this.fav = fav;
	}

}
