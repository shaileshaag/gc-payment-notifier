package com.gc.component;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.gc.component.listener.NotificationPanelListener;
import com.gc.provider.NotificationReceiver;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.SmsNotificationsSender;
import com.gc.vo.Notification;

public class NotificationPanel implements ComponentGroupPanel, NotificationReceiver {

	private final String sendNotificationsButtonText;

	private final EmailNotificationsSender emailNotificationSender;

	private final SmsNotificationsSender smsNotificationSender;

	private JScrollPane js;

	private JButton sendButton;

	private JFrame parentFrame;

	public NotificationPanel(String sendNotificationsButtonText, EmailNotificationsSender emailNotificationSender,
			SmsNotificationsSender smsNotificationSender) {
		this.sendNotificationsButtonText = sendNotificationsButtonText;
		this.emailNotificationSender = emailNotificationSender;
		this.smsNotificationSender = smsNotificationSender;
	}

	public void init() {
		PaymentTable table = new PaymentTable(new Object[0][], Notification.HEADERS);

		table.setFillsViewportHeight(true);
		table.setVisible(false);
		js = new JScrollPane(table);

		sendButton = new JButton(sendNotificationsButtonText);
		sendButton.setEnabled(false);
	}

	@Override
	public Group getVerticalComponents(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(js).addComponent(sendButton);
	}

	@Override
	public Group getHorizontalComponents(GroupLayout groupLayout) {
		return groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(js).addComponent(sendButton);
	}

	@Override
	public void receive(final List<Notification> notifications) {
		removeAllSendButtonListeners();
		PaymentTable table = null;
		if (notifications.isEmpty()) {
			sendButton.setEnabled(false);
			table = new PaymentTable(new Object[0][], Notification.HEADERS);
		} else {
			Object[][] notificationsData = notifications.stream().map(n -> n.getTableDataArray())
					.collect(Collectors.toList()).toArray(new Object[0][]);
			NotificationPanelListener npl = new NotificationPanelListener(parentFrame, notifications,
					emailNotificationSender, smsNotificationSender);
			table = new PaymentTable(notificationsData, Notification.HEADERS);
			table.getModel().addTableModelListener(npl);
			table.setFillsViewportHeight(true);
			sendButton.addActionListener(npl);
			sendButton.setEnabled(true);
		}
		js.setViewportView(table);
	}

	private void removeAllSendButtonListeners() {
		ActionListener[] actionListeners = sendButton.getActionListeners();
		for (ActionListener al : actionListeners) {
			sendButton.removeActionListener(al);
		}
	}

	@Override
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}
