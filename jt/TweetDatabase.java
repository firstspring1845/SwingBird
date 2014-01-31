package jt;

import twitter4j.*;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetDatabase
{

	static class AsyncListener extends TwitterAdapter
	{

		@Override
		public void createdFavorite(Status status)
		{
			TweetDatabase.createStatusModel(status).setFavorite(true);
			Main.bar.setStatus("ふぁぼふぁぼしました", Color.green);
			Main.refresh();
		}

		@Override
		public void destroyedFavorite(Status status)
		{
			TweetDatabase.createStatusModel(status).setFavorite(false);
			Main.bar.setStatus("あんふぁぼりました", Color.green);
			Main.refresh();
		}

		@Override
		public void updatedStatus(Status status)
		{
			TweetDatabase.createStatusModel(status);
			Main.bar.setStatus("ツイーヨしました", Color.green);
		}

		@Override
		public void retweetedStatus(Status status)
		{
			TweetDatabase.createStatusModel(status);
			Main.bar.setStatus("リツイーヨしました", Color.green);
		}

		@Override
		public void onException(TwitterException te, TwitterMethod method)
		{
			Main.bar.setStatus("何かがおかしいです", Color.red);
		}
	}

	public static final TwitterListener asyncListener = new AsyncListener();

	@SuppressWarnings("unchecked")
	private static Map<Long, StatusModel> statuses = Collections.synchronizedMap(new HashMap());
	@SuppressWarnings("unchecked")
	private static Map<Long, User> users = Collections.synchronizedMap(new HashMap());

	public static User getUser(Long id)
	{
		return users.get(id);
	}

	public static void putUser(User u)
	{
		users.put(u.getId(), u);
	}

	public static StatusModel getStatus(Long id)
	{
		return statuses.get(id);
	}

	public static StatusModel createStatusModel(Status s)
	{
		if (s.isRetweet())
		{
			if (!statuses.containsKey(s.getRetweetedStatus().getId()))
			{
				statuses.put(s.getRetweetedStatus().getId(), new StatusModel(s.getRetweetedStatus()));
			}
		}
		if (statuses.containsKey(s.getId()))
		{
			return statuses.get(s.getId());
		}
		StatusModel m = new StatusModel(s);
		statuses.put(s.getId(), m);
		return m;
	}

	public static Long[] processList(List<Status> l)
	{
		Long[] a = new Long[l.size()];
		for (int i = 0; i < l.size(); i++)
		{
			createStatusModel(l.get(i));
			a[i] = l.get(i).getId();
		}
		return a;
	}

}
