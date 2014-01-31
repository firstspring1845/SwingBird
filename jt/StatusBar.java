package jt;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusBar extends JPanel
{
	Color def = this.getBackground();

	Color current = def;

	private Timer t;

	private int progress = 1;
	public static final int max = 500;

	private JLabel bar;

	public StatusBar()
	{
		this.setLayout(new BorderLayout());
		this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		t = new Timer(1, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				float rOffset = def.getRed() - current.getRed();
				float gOffset = def.getGreen() - current.getGreen();
				float bOffset = def.getBlue() - current.getBlue();
				float p = 1F * progress / max;
				rOffset *= p;
				gOffset *= p;
				bOffset *= p;
				int r = (int) (current.getRed() + rOffset);
				int g = (int) (current.getGreen() + gOffset);
				int b = (int) (current.getBlue() + bOffset);
				StatusBar.this.setBackground(new Color(r, g, b));
				progress += 1;
				if (progress == max)
				{
					t.stop();
				}
			}
		});
		t.start();
		bar = new JLabel("わいわい");
		this.add(bar, BorderLayout.CENTER);
	}

	public void setStatus(String text, Color c)
	{
		bar.setText(text);
		current = c;
		progress = 1;
		t.start();
	}
}
