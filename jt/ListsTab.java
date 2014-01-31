package jt;

import twitter4j.Twitter;
import twitter4j.UserList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ListsTab extends JScrollPane
{

	JList list = new JList<>();

	public ListsTab()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setViewportView(list);
		list.setCellRenderer(new DefaultListCellRenderer()
		{
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				UserList ul = (UserList) value;
				this.setText(ul.getName());
				return this;
			}
		});
		Object o = Serializer.read("lists.dat");
		if (o instanceof List)
		{
			List l = (List) o;
			list.setListData(l.toArray());
		}
		list.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_F5)
				{
					new Thread()
					{
						@Override
						public void run()
						{
							System.out.println("call");
							Twitter t = AccountManager.getInstance().getTwitter();
							try
							{
								final List l = t.getUserLists(t.getUserTimeline().get(0).getUser().getId());
								Serializer.write(l, "lists.dat");
								SwingUtilities.invokeLater(new Runnable()
								{
									@Override
									public void run()
									{
										list.setListData(l.toArray());
										Main.bar.setStatus("リスト一覧を更新しました", Color.green);
									}
								});
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}.start();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					JList<UserList> l = (JList<UserList>) e.getComponent();
					UserList ll = l.getSelectedValue();
					Main.tp.addTab(ll.getName(), new ListTimelineTab(ll.getId()));
				}
			}
		});
	}

}
