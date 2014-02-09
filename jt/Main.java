package jt;

import twitter4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{

	public static final String ACCOUNT = "A";

	public static final Main INSTANCE = new Main();

	public static final JMenuBar menu = new JMenuBar();
	public static final JPanel mainPanel = new JPanel();
	public static final JTabbedPane tp = new JTabbedPane();
	public static final StatusBar bar = new StatusBar();

	public static void main(String... args) throws Exception
	{
		AccountManager.load();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(3);
		f.setSize(480, 640);

		JMenu m = new JMenu("設定");
		menu.add(m);

		JMenuItem i = new JMenuItem("アカウント設定");
		i.setActionCommand(ACCOUNT);
		i.addActionListener(INSTANCE);
		m.add(i);

		f.add(menu, BorderLayout.NORTH);
		f.add(tp);
		f.add(bar, BorderLayout.SOUTH);
		tp.add(new HomeTimelineTab(), "Home");
		tp.add(new ListsTab(), "List");
		tp.add(new HistoryTab(), "History");
		f.setVisible(true);
		TwitterStream ts = AccountManager.getInstance().getTwitterStream();
		ts.addListener(new UserStreamAdapter()
		{
			@Override
			public void onStatus(Status s)
			{
				TweetDatabase.createStatusModel(s);
				for (Component c : tp.getComponents())
				{
					if (c instanceof TweetListener)
					{
						TweetListener l = (TweetListener) c;
						l.onStatus(s);
					}
				}
			}

			@Override
			public void onFavorite(User u, User uu, Status s)
			{
				TweetDatabase.putUser(u);
				TweetDatabase.createStatusModel(s).setFavorite(true);
				Main.refresh();
				for (Component c : Main.tp.getComponents())
				{
					if (c instanceof ActivityListener)
					{
						ActivityListener l = (ActivityListener) c;
						l.onFavorite(u, uu, s);
					}
				}
			}

			@Override
			public void onUnfavorite(User u, User uu, Status s)
			{
				TweetDatabase.putUser(u);
				TweetDatabase.createStatusModel(s).setFavorite(false);
				Main.refresh();
				for (Component c : Main.tp.getComponents())
				{
					if (c instanceof ActivityListener)
					{
						ActivityListener l = (ActivityListener) c;
						l.onUnfavorite(u, uu, s);
					}
				}
			}

			@Override
			public void onFollow(User u, User uu)
			{
				TweetDatabase.putUser(u);
				TweetDatabase.putUser(uu);
				for (Component c : Main.tp.getComponents())
				{
					if (c instanceof ActivityListener)
					{
						ActivityListener l = (ActivityListener) c;
						l.onFollow(u, uu);
					}
				}
			}
		});
		ts.addConnectionLifeCycleListener(new ConnectionLifeCycleListener()
		{
			@Override
			public void onConnect()
			{
				Main.bar.setStatus("接続しました", Color.green);
			}

			@Override
			public void onDisconnect()
			{
				Main.bar.setStatus("接続が切れました", Color.red);
			}

			@Override
			public void onCleanUp()
			{

			}
		});
		ts.user();
	}

	public static void refresh()
	{
		tp.getSelectedComponent().repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case ACCOUNT:
				new AccountDialog().setVisible(true);
				break;
		}
	}
}