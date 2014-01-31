package jt;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class JFlatButton extends JLabel implements MouseListener
{

	private List<ActionListener> actionListenerList = new ArrayList<>();
	private static final Color COLOR_OFF = new Color(215, 215, 215);
	private static final Color COLOR_ON = new Color(190, 190, 190);

	public JFlatButton(String text, int width)
	{
		super(text, JLabel.CENTER);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.GRAY));
		addMouseListener(this);
		setOpaque(true);
		this.setPreferredSize(new Dimension(width, 30));
	}

	public void addActionListener(ActionListener listener)
	{
		actionListenerList.add(listener);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		setBackground(COLOR_ON);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		setBackground(COLOR_OFF);
		if (isInBox(e.getPoint()))
		{
			ActionEvent ae = new ActionEvent(this, e.getID(), "");
			for (ActionListener listener : actionListenerList)
			{
				listener.actionPerformed(ae);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		setBackground(COLOR_OFF);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		setBackground(Color.WHITE);
	}

	public boolean isInBox(Point point)
	{
		return 0 <= point.x && 0 <= point.y && point.x <= this.getWidth()
				&& point.y <= this.getHeight();
	}
}