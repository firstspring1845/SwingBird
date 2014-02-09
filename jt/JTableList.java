package jt;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

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
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "selectFirstRow");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "selectLastRow");
		im.put(KeyStroke.getKeyStroke('j'), "selectNextRow");
		im.put(KeyStroke.getKeyStroke('k'), "selectPreviousRow");
		Set<AWTKeyStroke> keyset;
		keyset = new HashSet(getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		keyset.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keyset);
		keyset = new HashSet(getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		keyset.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, keyset);
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
