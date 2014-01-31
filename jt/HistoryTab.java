package jt;

import twitter4j.Status;
import twitter4j.User;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HistoryTab extends ListTab
{
	public HistoryTab()
	{
		super();
		list.setDefaultRenderer(Object.class, ActivityRender.INSTANCE);
		list.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyChar())
				{
					case 'j':
						list.changeSelection(list.getSelectedRow() + 1, 0, false, false);
						break;
					case 'k':
						list.changeSelection(list.getSelectedRow() - 1, 0, false, false);
				}
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_HOME:
						list.changeSelection(0, 0, false, false);
						break;
					case KeyEvent.VK_END:
						list.changeSelection(list.getRowCount() - 1, 0, false, false);
						break;
				}
			}
		});
	}

	public void onFavorite(User from, User to, Status status)
	{
		String mine = AccountManager.getInstance().selected;
		if (isAccept(from, to))
		{
			list.addTop(new ActivityEvent(from, EventType.FAV, status.getText()));
		}
	}

	public void onUnfavorite(User from, User to, Status status)
	{
		if (isAccept(from, to))
		{
			list.addTop(new ActivityEvent(from, EventType.UNFAV, status.getText()));
		}
	}

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
