package jt;

import twitter4j.*;

@SuppressWarnings("serial")
public class ListTimelineTab extends TimelineTab
{

	public long top = 1;
	public long last = Long.MAX_VALUE;

	public ListTimelineTab(int listid)
	{
		super();
		this.listid = listid;
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();

		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserListStatuses(ResponseList<Status> statuses)
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
		t.getUserListStatuses(listid, new Paging(1, 200));
	}

	public static int listid = 101022308;

	@Override
	public void refresh()
	{
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserListStatuses(ResponseList<Status> statuses)
			{
				final Long[] ids = TweetDatabase.processList(statuses);
				if (ids.length != 0)
				{
					top = ids[0];
				}
				refresh(ids);
			}
		});
		t.getUserListStatuses(listid, new Paging(1, 200, top));
	}

	@Override
	public void fetchMore()
	{
		AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
		t.addListener(new TwitterAdapter()
		{
			@Override
			public void gotUserListStatuses(ResponseList<Status> statuses)
			{
				final Long[] ids = TweetDatabase.processList(statuses);
				if (ids.length != 0)
				{
					last = ids[ids.length - 1];
				}
				fetchMore(ids);
			}
		});
		t.getUserListStatuses(listid, new Paging(1, 200, 1, last));

	}

}
