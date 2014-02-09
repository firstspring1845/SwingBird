package jt;

import twitter4j.Status;
import twitter4j.User;

public class HistoryTab extends ListTab implements TweetListener, ActivityListener
{
	public HistoryTab()
	{
		super();
		list.setDefaultRenderer(Object.class, ActivityRender.INSTANCE);
	}

	@Override
	public void onStatus(Status status)
	{
		if(status.isRetweet())
		{
			if(status.getRetweetedStatus().getUser().getScreenName().equals(AccountManager.getInstance().selected))
			{
				list.addTop(new ActivityEvent(status.getUser(), EventType.RT, status.getRetweetedStatus().getText()));
			}
		}
	}

	@Override
	public void onFavorite(User from, User to, Status status)
	{
		if (isAccept(from, to))
		{
			list.addTop(new ActivityEvent(from, EventType.FAV, status.getText()));
		}
	}

	@Override
	public void onUnfavorite(User from, User to, Status status)
	{
		if (isAccept(from, to))
		{
			list.addTop(new ActivityEvent(from, EventType.UNFAV, status.getText()));
		}
	}

	@Override
	public void onFollow(User from, User to)
	{
		if (isAccept(from, to))
		{
			list.addTop(new ActivityEvent(from, EventType.FOLLOW, ""));
		}
	}

	public static boolean isAccept(User from, User to)
	{
		String mine = AccountManager.getInstance().selected;
		return !mine.equals(from.getScreenName()) && mine.equals(to.getScreenName());
	}

}
