package jt;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthDialog extends JDialog implements ActionListener
{

	JPanel panel;
	JTextField pin;
	Twitter t;
	RequestToken rt;

	public AuthDialog() throws TwitterException
	{
		this.setSize(420, 200);
		this.setResizable(false);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		t = new TwitterFactory().getInstance();
		t.setOAuthConsumer(AccountManager.KEY, AccountManager.SECRET);
		rt = t.getOAuthRequestToken();

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(200, 200, 200));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(mainPanel, "Center");

		JTextField url = new JTextField(rt.getAuthenticationURL(), 35);
		mainPanel.add(url);
		JLabel label = new JLabel("<html>上のURLをブラウザにコピペして認証し<br>出力される7桁の数字を下のボックスに入力してください");
		mainPanel.add(label);
		pin = new JTextField("", 5);
		mainPanel.add(pin);
		JPanel buttonPanel = new JPanel();
		JFlatButton button = new JFlatButton("OK", 50);
		button.addActionListener(this);
		buttonPanel.add(button);
		panel.add(buttonPanel, "Last");

		this.add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			AccessToken token = t.getOAuthAccessToken(rt, pin.getText());
			AccountManager.getInstance().put(token.getScreenName(), token);
			AccountManager.save();
		} catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, "何かがおかしいです");
		}
		this.setVisible(false);
	}

}