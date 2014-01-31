package jt;

import twitter4j.*;

import javax.swing.*;

public class HomeTimelineTab extends TimelineTab
{

	public long top = 1;
	public long last = Long.MAX_VALUE;

	public HomeTimelineTab()
	{
		super();
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserTimeline(ResponseList<Status> statuses)
			{
				final Long[] ids = TweetDatabase.processList(statuses);
				if (ids.length != 0)
				{
					top = ids[0];
					last = ids[ids.length - 1];
				}
				refresh(ids);
			}
		});
		t.getUserTimeline(new Paging(1, 20));
	}

	@Override
	public void refresh()
	{
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserTimeline(ResponseList<Status> statuses)
			{
				final Long[] ids = TweetDatabase.processList(statuses);
				if (ids.length != 0)
				{
					top = ids[0];
				}
				refresh(ids);
			}
		});
		t.getUserTimeline(new Paging(1, 200, top));
	}

	@Override
	public void fetchMore()
	{
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserTimeline(ResponseList<Status> statuses)
			{
				final Long[] ids = TweetDatabase.processList(statuses);
				if (ids.length != 0)
				{
					last = ids[ids.length - 1];
				}
				fetchMore(ids);
			}
		});
		t.getUserTimeline(new Paging(1, 200, 1, last));
	}

	@Override
	public void onTweet(final Long id)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				list.addTop(id);
			}
		});
	}

}
