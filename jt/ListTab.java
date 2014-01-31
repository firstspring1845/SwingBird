package jt;

import jt.JTableList;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class ListTab extends JScrollPane
{

	JTableList list = new JTableList();

	public ListTab()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setViewportView(list);
	}

}