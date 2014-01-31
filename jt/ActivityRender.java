package jt;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActivityRender extends JPanel implements TableCellRenderer
{
	public static final ActivityRender INSTANCE = new ActivityRender();

	public int height;

	JLabel icon;
	JTextArea event;
	JTextArea text;
	JTextArea time;

	public ActivityRender()
	{
		this.setLayout(null);
		icon = new JLabel(new ImageIcon("a.png"));
		this.add(icon);
		icon.setSize(48, 48);
		icon.setLocation(5, 5);

		event = new JTextArea();
		event.setEditable(false);
		event.setLineWrap(true);
		this.add(event);

		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		this.add(text);

		time = new JTextArea();
		time.setEditable(false);
		time.setLineWrap(true);
		this.add(time);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		ActivityEvent e = (ActivityEvent) value;

		if (isSelected)
		{
			setColor(Color.green);
		} else
		{
			setColor(Color.white);
		}

		for (Component c : this.getComponents())
		{
			c.setFont(new Font("Meiryo", Font.PLAIN, 12));
		}

		icon.setIcon(IconManager.getImage(e.getUser().getProfileImageURL()));

		event.setText(e.getUser().getScreenName() + "に" + e.type.message);
		event.setLocation(48 + 10, 0);
		event.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		event.setSize(table.getSize().width - 48 - 10, event.getPreferredSize().height);

		text.setText(e.text);
		text.setLocation(48 + 10, event.getLocation().y + event.getPreferredSize().height);
		text.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		text.setSize(table.getSize().width - 48 - 10, text.getPreferredSize().height);

		time.setText(e.time.toString());
		time.setLocation(48 + 10, text.getLocation().y + text.getPreferredSize().height);
		time.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		time.setSize(table.getSize().width - 48 - 10, time.getPreferredSize().height);

		height = Math.max(58, event.getPreferredSize().height + text.getPreferredSize().height + time.getPreferredSize().height);
		this.setSize(0, height);

		//this.setPreferredSize(new Dimension(list.getSize().width - 48 - 10, Math.max(58, lineHeight + 1)));
		return this;
	}

	void setColor(Color c)
	{
		this.setBackground(c);
		icon.setBackground(c);
		event.setBackground(c);
		text.setBackground(c);
		time.setBackground(c);
	}
}
