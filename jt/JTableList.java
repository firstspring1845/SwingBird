package jt;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JTableList extends JTable
{

	public DefaultTableModel model = new DefaultTableModel(new Object[]{"laco"}, 0);

	//レンダーが返すコンポーネントのHeight取るからsetSizeしてやって
	//TODO:getPreferredSize使うといい気がするけど面倒だから気が向いたら検証する
	public JTableList()
	{
		super();
		this.setTableHeader(null);
		this.setIntercellSpacing(new Dimension(0, 1));
		this.setDefaultEditor(Object.class, null);
		this.setModel(model);
		InputMap im = getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "NOTHING");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK), "NOTHING");
		this.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				JTableList list = (JTableList) e.getComponent();
				switch (e.getKeyChar())
				{
					case 'j':
						if (list.getSelectedRow() != list.getRowCount() - 1)
						{
							list.changeSelection(list.getSelectedRow() + 1, 0, false, false);
						}
						break;
					case 'k':
						list.changeSelection(list.getSelectedRow() - 1, 0, false, false);
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
				}
			}
		});
	}

	public boolean dirty = true;

	@Override
	public void doLayout()
	{
		if (dirty)
		{
			initPreferredHeight();
		}
		super.doLayout();
	}

	@Override
	public void columnMarginChanged(ChangeEvent e)
	{
		dirty = true;
		super.columnMarginChanged(e);
	}

	private void initPreferredHeight()
	{
		for (int i = 0; i < this.getRowCount(); i++)
		{
			this.setRowHeight(i, prepareRenderer(this.getCellRenderer(i, 0), i, 0).getHeight());
		}
		dirty = false;
	}

	public void addTop(Object o)
	{
		model.insertRow(0, new Object[]{o});
		this.setRowHeight(0, prepareRenderer(this.getCellRenderer(0, 0), 0, 0).getHeight());
		if (getSelectedRow() == 0)
		{
			changeSelection(1, 0, false, false);
		}
	}

	public void addLast(Object o)
	{
		model.addRow(new Object[]{o});
		this.setRowHeight(model.getRowCount() - 1, prepareRenderer(this.getCellRenderer(model.getRowCount() - 1, 0), model.getRowCount() - 1, 0).getHeight());
	}

}
