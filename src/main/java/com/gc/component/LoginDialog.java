package com.gc.component;

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

	private static final String SEND = "Send";

	private static final String CANCEL = "Cancel";

	private JTextField tfUsername;

	private JPasswordField pfPassword;

	private boolean succeeded;

	public LoginDialog(JFrame parentFrame) {
		super(parentFrame, "Login", true);

		JLabel lbUsername = new JLabel("Email Id: ");
		tfUsername = new JTextField(20);
		JLabel lbEmail = new JLabel("@gmail.com");
		JLabel lbPassword = new JLabel("Password: ");
		pfPassword = new JPasswordField(20);
		JLabel emptyLbl = new JLabel("                   ");

		JButton btnLogin = new JButton(SEND);
		btnLogin.addActionListener(this);

		JButton btnCancel = new JButton(CANCEL);
		btnCancel.addActionListener(this);

		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(lbUsername).addGap(10, 20, 100)
						.addComponent(tfUsername).addComponent(lbEmail).addGap(30))
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(lbPassword).addGap(10, 20, 100)
						.addComponent(pfPassword).addComponent(emptyLbl).addGap(30))
				.addGroup(
						groupLayout.createSequentialGroup().addGap(30).addComponent(btnLogin).addGap(30).addComponent(btnCancel)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbUsername)
						.addComponent(tfUsername).addComponent(lbEmail))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lbPassword)
						.addComponent(pfPassword).addComponent(emptyLbl))
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
			if (StringUtils.isEmpty(getUsername()) || StringUtils.isEmpty(getPassword())) {
				JOptionPane.showMessageDialog(getParent(), "No username of password", "Alert",
						JOptionPane.WARNING_MESSAGE);
			} else {
				dispose();
				succeeded = true;
			}
		} else if (source.getText().equals(CANCEL)) {
			dispose();
		}
	}

	public String getUsername() {
		return tfUsername.getText();
	}

	public String getPassword() {
		return new String(pfPassword.getPassword());
	}

	public boolean isSucceeded() {
		return succeeded;
	}

}
