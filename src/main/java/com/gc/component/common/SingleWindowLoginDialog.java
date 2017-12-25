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

import org.springframework.util.StringUtils;

import com.gc.vo.SingleWindowLogin;

public class SingleWindowLoginDialog extends JDialog implements ActionListener, LoginDialog {

	private static final long serialVersionUID = 1L;

	private static final String SEND = "Send";

	private static final String CANCEL = "Cancel";

	private final SingleWindowLogin singleWindowLogin;

	private JPasswordField singlePassword;

	private boolean succeeded;

	public SingleWindowLoginDialog(JFrame parentFrame, SingleWindowLogin singleWindowLogin) {
		super(parentFrame, "Login", true);

		this.singleWindowLogin = singleWindowLogin;

		JLabel emailPasswordLabel = new JLabel("Password:         ");
		singlePassword = new JPasswordField(20);
		JLabel emptyLbl = new JLabel("                   ");

		JButton btnLogin = new JButton(SEND);
		btnLogin.addActionListener(this);

		JButton btnCancel = new JButton(CANCEL);
		btnCancel.addActionListener(this);

		Container contentPanel = getContentPane();
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		contentPanel.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(emailPasswordLabel)
						.addComponent(singlePassword).addComponent(emptyLbl).addGap(30))
				.addGroup(groupLayout.createSequentialGroup().addGap(30).addComponent(btnLogin).addGap(30)
						.addComponent(btnCancel)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(emailPasswordLabel).addComponent(singlePassword).addComponent(emptyLbl))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnLogin)
						.addComponent(btnCancel)));
		pack();
		setResizable(false);
		setLocationRelativeTo(parentFrame);
		getRootPane().setDefaultButton(btnLogin);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.getText().equals(SEND)) {
			if (StringUtils.isEmpty(getEmailPassword())) {
				JOptionPane.showMessageDialog(getParent(), "No password", "Alert", JOptionPane.WARNING_MESSAGE);
			} else {
				dispose();
				succeeded = true;
			}
		} else if (source.getText().equals(CANCEL)) {
			dispose();
		}
	}

	public String getEmailUsername() {
		return singleWindowLogin.getEmailUser();
	}

	public String getEmailPassword() {
		return new String(singlePassword.getPassword());
	}

	public String getSmsUsername() {
		return singleWindowLogin.getSmsUser();
	}

	public String getSmsPassword() {
		return new String(singlePassword.getPassword());
	}

	public boolean isSucceeded() {
		return succeeded;
	}

}
