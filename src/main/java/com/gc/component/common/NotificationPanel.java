package com.gc.component.common;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import com.gc.component.listener.NotificationPanelListener;
import com.gc.provider.NotificationReceiver;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.SmsNotificationsSender;
import com.gc.vo.Notification;

public class NotificationPanel implements ComponentGroupPanel, NotificationReceiver {

	private final String sendNotificationsButtonText;

	private final EmailNotificationsSender emailNotificationSender;

	private final SmsNotificationsSender smsNotificationSender;

	private final NotificationTableColumnCheckboxDecider checkboxDecider;

	public final String[] headers;

	private JScrollPane js;

	private JButton sendButton;

	private JCheckBox selectAllEmail;

	private JCheckBox selectAllSMS;

	private JFrame parentFrame;

	public NotificationPanel(String sendNotificationsButtonText, EmailNotificationsSender emailNotificationSender,
			SmsNotificationsSender smsNotificationSender, NotificationTableColumnCheckboxDecider checkboxDecider,
			String[] headers) {
		this.sendNotificationsButtonText = sendNotificationsButtonText;
		this.emailNotificationSender = emailNotificationSender;
		this.smsNotificationSender = smsNotificationSender;
		this.checkboxDecider = checkboxDecider;
		this.headers = headers;
	}

	public void init() {
		NotificationTable table = new NotificationTable(checkboxDecider, new Object[0][], headers);

		table.setFillsViewportHeight(true);
		table.setVisible(false);
		js = new JScrollPane(table);

		sendButton = new JButton(sendNotificationsButtonText);
		sendButton.setEnabled(false);
		sendButton.setActionCommand(NotificationPanelListener.SEND_NOTIFICATION_ACTION_COMMAND);

		selectAllEmail = new JCheckBox("Select All Emails");
		selectAllEmail.setSelected(true);
		selectAllEmail.setEnabled(false);
		selectAllEmail.setActionCommand(NotificationPanelListener.SELECT_ALL_EMAILS_ACTION_COMMAND);
		selectAllSMS = new JCheckBox("Select All SMS");
		selectAllSMS.setSelected(true);
		selectAllSMS.setEnabled(false);
		selectAllSMS.setActionCommand(NotificationPanelListener.SELECT_ALL_SMS_ACTION_COMMAND);
	}

	@Override
	public Group getVerticalComponents(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup().addComponent(selectAllEmail).addComponent(selectAllSMS))
				.addComponent(js).addComponent(sendButton);
	}

	@Override
	public Group getHorizontalComponents(GroupLayout groupLayout) {
		return groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup().addComponent(selectAllEmail).addComponent(selectAllSMS))
				.addComponent(js).addComponent(sendButton);
	}

	@Override
	public void receive(final List<? extends Notification> notifications) {
		removeAllListeners();
		NotificationTable table = null;
		if (notifications.isEmpty()) {
			sendButton.setEnabled(false);
			selectAllEmail.setSelected(true);
			selectAllEmail.setEnabled(false);
			selectAllSMS.setSelected(true);
			selectAllSMS.setEnabled(false);
			table = new NotificationTable(checkboxDecider, new Object[0][], headers);
		} else {
			Object[][] notificationsData = notifications.stream().map(n -> n.getTableDataArray())
					.collect(Collectors.toList()).toArray(new Object[0][]);
			table = new NotificationTable(checkboxDecider, notificationsData, headers);
			table.setFillsViewportHeight(true);
			TableModel tableModel = table.getModel();
			NotificationPanelListener npl = new NotificationPanelListener(parentFrame, notifications,
					emailNotificationSender, smsNotificationSender, table);
			tableModel.addTableModelListener(npl);
			sendButton.addActionListener(npl);
			sendButton.setEnabled(true);
			selectAllEmail.setSelected(true);
			selectAllEmail.setEnabled(true);
			selectAllEmail.addActionListener(npl);
			selectAllSMS.setSelected(true);
			selectAllSMS.setEnabled(true);
			selectAllSMS.addActionListener(npl);
		}
		js.setViewportView(table);
	}

	private void removeAllListeners() {
		ActionListener[] actionListeners = sendButton.getActionListeners();
		for (ActionListener al : actionListeners) {
			sendButton.removeActionListener(al);
		}
		actionListeners = selectAllEmail.getActionListeners();
		for (ActionListener al : actionListeners) {
			selectAllEmail.removeActionListener(al);
		}
		actionListeners = selectAllSMS.getActionListeners();
		for (ActionListener al : actionListeners) {
			selectAllSMS.removeActionListener(al);
		}
	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}
