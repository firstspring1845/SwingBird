package jt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountDialog extends JDialog implements ActionListener
{
	JPanel panel;

	JFlatButton buttonOK;
	JFlatButton buttonCancel;
	JFlatButton buttonAuth;

	JList<String> accountList;

	public AccountDialog()
	{
		super();

		this.setSize(480, 640);
		this.setModalityType(ModalityType.APPLICATION_MODAL);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(200, 200, 200));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(mainPanel, "Center");

		JPanel ynPanel = new JPanel();
		buttonOK = new JFlatButton("OK", 100);
		buttonCancel = new JFlatButton("キャンセル", 100);
		buttonAuth = new JFlatButton("アカウント認証", 100);
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonAuth.addActionListener(this);
		ynPanel.add(buttonOK);
		ynPanel.add(buttonCancel);
		ynPanel.add(buttonAuth);
		AccountManager.load();
		accountList = new JList<>(AccountManager.getInstance().getAccounts());
		mainPanel.add(accountList, "Center");
		panel.add(ynPanel, "Last");

		this.add(panel);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttonOK)
		{
			String acc = accountList.getSelectedValue();
			if (acc != null)
			{
				AccountManager.getInstance().selected = acc;
				AccountManager.save();
				//NTListener.getInstance().connect();
			}
		}
		if (e.getSource() != buttonAuth)
		{
			Component c = SwingUtilities.getWindowAncestor(panel);
			c.setVisible(false);
		} else
		{
			try
			{
				new AuthDialog().setVisible(true);
			} catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "通信に失敗しました");
			}

			accountList.setListData(AccountManager.getInstance().getAccounts());
		}

	}
}