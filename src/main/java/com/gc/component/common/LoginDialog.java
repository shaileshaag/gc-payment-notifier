package com.gc.component.common;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.util.StringUtils;

public class LoginDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String SEND = "Send";

	private static final String CANCEL = "Cancel";

	private JTextField emailUsername;

	private JPasswordField emailPassword;

	private JTextField smsUsername;

	private JPasswordField smsPassword;

	private boolean succeeded;

	public LoginDialog(JFrame parentFrame) {
		super(parentFrame, "Login", true);

		JLabel emailUsernameLbl = new JLabel("Email Id:            ");
		emailUsername = new JTextField(20);
		JLabel emailDomainLabel = new JLabel("@gmail.com");
		JLabel emailPasswordLabel = new JLabel("Password:         ");
		emailPassword = new JPasswordField(20);
		JLabel emptyLbl = new JLabel("                   ");

		JLabel smsUsernameLbl = new JLabel("SMS User Id:      ");
		smsUsername = new JTextField(20);
		JLabel smsPasswordLabel = new JLabel("SMS Password:  ");
		smsPassword = new JPasswordField(20);
		JLabel emptyLbl2 = new JLabel("                   ");
		JLabel emptyLbl3 = new JLabel("                   ");

		JButton btnLogin = new JButton(SEND);
		btnLogin.addActionListener(this);

		JButton btnCancel = new JButton(CANCEL);
		btnCancel.addActionListener(this);

		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(emailUsernameLbl)
						.addComponent(emailUsername).addComponent(emailDomainLabel).addGap(30))
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(emailPasswordLabel)
						.addComponent(emailPassword).addComponent(emptyLbl).addGap(30))
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(smsUsernameLbl)
						.addComponent(smsUsername).addGap(30).addComponent(emptyLbl3))
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(smsPasswordLabel)
						.addComponent(smsPassword).addComponent(emptyLbl2).addGap(30))
				.addGroup(groupLayout.createSequentialGroup().addGap(30).addComponent(btnLogin).addGap(30)
						.addComponent(btnCancel)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(emailUsernameLbl)
						.addComponent(emailUsername).addComponent(emailDomainLabel))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(emailPasswordLabel).addComponent(emailPassword).addComponent(emptyLbl))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(smsUsernameLbl)
						.addComponent(smsUsername).addComponent(emptyLbl3))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(smsPasswordLabel)
						.addComponent(smsPassword).addComponent(emptyLbl2))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnLogin)
						.addComponent(btnCancel)));
		pack();
		setResizable(false);
		setLocationRelativeTo(parentFrame);
		getRootPane().setDefaultButton(btnLogin);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.getText().equals(SEND)) {
			if (StringUtils.isEmpty(getEmailUsername()) || StringUtils.isEmpty(getEmailPassword())) {
				JOptionPane.showMessageDialog(getParent(), "No Email username or password", "Alert",
						JOptionPane.WARNING_MESSAGE);
			} else if (StringUtils.isEmpty(getSmsUsername()) || StringUtils.isEmpty(getSmsPassword())) {
				JOptionPane.showMessageDialog(getParent(), "No SMS username or password", "Alert",
						JOptionPane.WARNING_MESSAGE);
			} else {
				dispose();
				succeeded = true;
			}
		} else if (source.getText().equals(CANCEL)) {
			dispose();
		}
	}

	public String getEmailUsername() {
		return emailUsername.getText();
	}

	public String getEmailPassword() {
		return new String(emailPassword.getPassword());
	}

	public String getSmsUsername() {
		return smsUsername.getText();
	}

	public String getSmsPassword() {
		return new String(smsPassword.getPassword());
	}

	public boolean isSucceeded() {
		return succeeded;
	}

}
