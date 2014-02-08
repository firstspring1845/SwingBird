package jt;

import twitter4j.AsyncTwitter;
import twitter4j.StatusUpdate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;

public abstract class TimelineTab extends ListTab
{

	public boolean refresh;
	public boolean fetchMore;

	public TimelineTab()
	{
		super();
		list.setDefaultRenderer(Object.class, StatusRenderer.INSTANCE);
		list.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{
				JTableList l = (JTableList) e.getComponent();
				AsyncTwitter t = AccountManager.getInstance().getAsyncTwitter();
				t.addListener(TweetDatabase.asyncListener);
				try
				{
					Long id = (Long) l.getValueAt(l.getSelectedRow(), 0);
					switch (e.getKeyChar())
					{
						case 'f':
							t.createFavorite(id);
							break;
						case 'r':
							t.retweetStatus(id);
							break;
						case 'u':
							t.destroyFavorite(id);
							break;
						case '@':
							t.updateStatus(new StatusUpdate(JOptionPane.showInputDialog("v('ω')", "@" + TweetDatabase.getStatus(id).getUser().getScreenName() + " ")).inReplyToStatusId(id));
							break;
					}
				} catch (Exception ex)
				{
				}
				switch (e.getKeyChar())
				{
					case 'c':
						clear();
						break;
					case 'j':
						list.changeSelection(list.getSelectedRow() + 1, 0, false, false);
						break;
					case 'k':
						list.changeSelection(list.getSelectedRow() - 1, 0, false, false);
						break;
					case 't':
						t.updateStatus(new StatusUpdate(JOptionPane.showInputDialog("v('ω')")));
						break;
				}
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_HOME:
						list.changeSelection(0, 0, false, false);
						break;
					case KeyEvent.VK_END:
						list.changeSelection(list.getRowCount() - 1, 0, false, false);
						break;
					case KeyEvent.VK_F5:
						if (!refresh)
						{
							refresh();
						}
						refresh = true;
						break;
					case KeyEvent.VK_F6:
						if (!fetchMore)
						{
							fetchMore();
						}
						fetchMore = true;
				}
			}
		});
	}

	// こいつらAsyncな感じで
	public void clear()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				list.model = new DefaultTableModel(new Object[]{"laco"}, 0);
				list.setModel(list.model);
			}
		});
	}

	public abstract void refresh();

	public abstract void fetchMore();

	public void refresh(final Long[] ids)
	{
		Collections.reverse(Arrays.asList(ids));
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				for (Long id : ids)
				{
					TimelineTab.this.list.addTop(id);
				}
				Main.bar.setStatus(new StringBuilder().append(ids.length).append("件取得しました").toString(), Color.green);
			}
		});
		refresh = false;
	}

	public void fetchMore(final Long[] ids)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for (Long id : ids)
				{
					TimelineTab.this.list.addLast(id);
				}
				Main.bar.setStatus(new StringBuilder().append(ids.length).append("件取得しました").toString(), Color.green);
			}
		});
		fetchMore = false;
	}

	public void onTweet(Long id)
	{
	}

}
