package com.gc.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.gc.component.LoginDialog;
import com.gc.component.SendNotificationsProgress;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.SmsNotificationsSender;
import com.gc.vo.Notification;

public class NotificationPanelListener implements TableModelListener, ActionListener {

	public static final String SEND_NOTIFICATION_ACTION_COMMAND = "SEND_NOTIFICATIONS";

	public static final String SELECT_ALL_EMAILS_ACTION_COMMAND = "SELECT_ALL_EMAILS";

	public static final String SELECT_ALL_SMS_ACTION_COMMAND = "SELECT_ALL_SMS";

	private final List<Notification> notifications;

	private final EmailNotificationsSender emailNotificationSender;

	private final SmsNotificationsSender smsNotificationSender;

	private final JFrame parentFrame;

	private final JTable table;

	public NotificationPanelListener(JFrame parentFrame, List<Notification> notifications,
			EmailNotificationsSender emailNotificationSender, SmsNotificationsSender smsNotificationSender,
			JTable table) {
		this.notifications = notifications;
		this.emailNotificationSender = emailNotificationSender;
		this.smsNotificationSender = smsNotificationSender;
		this.parentFrame = parentFrame;
		this.table = table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SEND_NOTIFICATION_ACTION_COMMAND == e.getActionCommand()) {
			LoginDialog ld = new LoginDialog(parentFrame);
			ld.setVisible(true);
			if (ld.isSucceeded()) {
				SendNotificationsProgress snp = new SendNotificationsProgress(parentFrame);
				ProgressListener emailProgressListener = snp.createProgressListener("Sent Emails");
				ProgressListener smsProgressListener = snp.createProgressListener("Sent SMS");
				Runnable emailSenderRunnable = () -> {
					emailNotificationSender.send(ld.getUsername(), ld.getPassword(), notifications,
							emailProgressListener);
					smsNotificationSender.send(notifications, smsProgressListener);
				};
				snp.addProcessOnLoad(emailSenderRunnable);
				snp.init();
				snp.setVisible(true);
			}
		} else if (SELECT_ALL_EMAILS_ACTION_COMMAND == e.getActionCommand()) {
			JCheckBox ch = (JCheckBox) e.getSource();
			boolean isSelected = ch.isSelected();
			for (int i = 0; i < notifications.size(); i++) {
				Notification notification = notifications.get(i);
				if (notification.getMemberDetail().canSendEmail()) {
					table.setValueAt(isSelected, i, 4);
				}
			}
			table.repaint();
		} else if (SELECT_ALL_SMS_ACTION_COMMAND == e.getActionCommand()) {
			JCheckBox ch = (JCheckBox) e.getSource();
			boolean isSelected = ch.isSelected();
			for (int i = 0; i < notifications.size(); i++) {
				Notification notification = notifications.get(i);
				if (notification.getMemberDetail().canSendSMS()) {
					table.setValueAt(isSelected, i, 5);
				}
			}
			table.repaint();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		TableModel tableModel = (TableModel) e.getSource();
		int row = e.getFirstRow();
		int column = e.getColumn();
		Boolean value = (Boolean) tableModel.getValueAt(row, column);
		Notification changedNotification = notifications.get(row);
		if (column == 4) {
			changedNotification.setSendEmail(value);
		} else if (column == 5) {
			changedNotification.setSendSms(value);
		}
	}

}
