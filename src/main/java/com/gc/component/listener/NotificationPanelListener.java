package com.gc.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.gc.component.LoginDialog;
import com.gc.component.SendNotificationsProgress;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.SmsNotificationsSender;
import com.gc.vo.Notification;

public class NotificationPanelListener implements TableModelListener, ActionListener {

	private final List<Notification> notifications;

	private final EmailNotificationsSender emailNotificationSender;

	private final SmsNotificationsSender smsNotificationSender;

	private final JFrame parentFrame;

	public NotificationPanelListener(JFrame parentFrame, List<Notification> notifications,
			EmailNotificationsSender emailNotificationSender, SmsNotificationsSender smsNotificationSender) {
		this.notifications = notifications;
		this.emailNotificationSender = emailNotificationSender;
		this.smsNotificationSender = smsNotificationSender;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LoginDialog ld = new LoginDialog(parentFrame);
		ld.setVisible(true);
		if (ld.isSucceeded()) {
			SendNotificationsProgress snp = new SendNotificationsProgress(parentFrame);
			ProgressListener emailProgressListener = snp.createProgressListener("Sent Emails");
			ProgressListener smsProgressListener = snp.createProgressListener("Sent SMS");
			Runnable emailSenderRunnable = () -> {
				emailNotificationSender.send(ld.getUsername(), ld.getPassword(), notifications, emailProgressListener);
				smsNotificationSender.send(notifications, smsProgressListener);
			};
			snp.addProcessOnLoad(emailSenderRunnable);
			snp.init();
			snp.setVisible(true);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		TableModel tableSource = (TableModel) e.getSource();
		int row = e.getFirstRow();
		int column = e.getColumn();
		Boolean value = (Boolean) tableSource.getValueAt(row, column);
		Notification changedNotification = notifications.get(row);
		if (column == 4) {
			changedNotification.setSendEmail(value);
		} else if (column == 5) {
			changedNotification.setSendSms(value);
		}
	}

}
