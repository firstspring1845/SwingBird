package jt;

public class StatusTable extends JTableList
{
	public static final StatusRenderer render = StatusRenderer.INSTANCE;

	public StatusTable()
	{
		super();
		this.setDefaultRenderer(Object.class, render);
	}

}