package jt;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class StatusRenderer extends JPanel implements TableCellRenderer
{
	public static final StatusRenderer INSTANCE = new StatusRenderer();

	public int height;

	JLabel icon;
	JTextArea name;
	JTextArea text;
	JTextArea via;

	public StatusRenderer()
	{
		this.setLayout(null);
		icon = new JLabel(new ImageIcon("a.png"));
		this.add(icon);
		icon.setSize(48, 48);
		icon.setLocation(5, 5);

		name = new JTextArea();
		name.setEditable(false);
		name.setLineWrap(true);
		this.add(name);

		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		this.add(text);

		via = new JTextArea();
		via.setEditable(false);
		via.setLineWrap(true);
		this.add(via);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		StatusModel s = TweetDatabase.getStatus((Long) value);
		if ((table.getRowCount() - row) % 2 == 0)
		{
			setColor(Color.white);
		} else
		{
			setColor(new Color(224, 224, 224));
		}
		if (s.isFavorited())
		{
			setColor(new Color(255, 204, 51));
		}
		if (isSelected)
		{
			setColor(new Color(128, 255, 128));
		}

		for (Component c : this.getComponents())
		{
			c.setFont(new Font("Meiryo", Font.PLAIN, 12));
		}

		icon.setIcon(IconCache.getImage(s.getIconURL()));

		name.setText(s.getName());
		name.setLocation(48 + 10, 0);
		// getPreferredSizeの値、キャッシュする場合があるからヤクイ by @noko_k
		name.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		name.setSize(table.getSize().width - 48 - 10, name.getPreferredSize().height);
		// System.out.println(event.getText());
		// System.out.println(event.getPreferredSize());

		text.setText(s.getText());
		text.setLocation(48 + 10, name.getLocation().y + name.getPreferredSize().height);
		text.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		text.setSize(table.getSize().width - 48 - 10, text.getPreferredSize().height);
		// System.out.println(text.getPreferredSize());

		via.setText(s.getSource());
		via.setLocation(48 + 10, text.getLocation().y + text.getPreferredSize().height);
		via.setSize(table.getSize().width - 48 - 10, Integer.MAX_VALUE);
		via.setSize(table.getSize().width - 48 - 10, via.getPreferredSize().height);

		height = Math.max(58, name.getPreferredSize().height + text.getPreferredSize().height + via.getPreferredSize().height);
		this.setSize(0, height);

		//this.setPreferredSize(new Dimension(list.getSize().width - 48 - 10, Math.max(58, lineHeight + 1)));
		return this;
	}

	void setColor(Color c)
	{
		this.setBackground(c);
		icon.setBackground(c);
		name.setBackground(c);
		text.setBackground(c);
		via.setBackground(c);
	}

}
