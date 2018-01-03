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

import com.gc.component.common.LoginDialog;
import com.gc.component.common.NotificationTableColumnCheckboxDecider;
import com.gc.component.common.SendNotificationsProgress;
import com.gc.component.common.SimpleLoginDialog;
import com.gc.component.common.SingleWindowLoginDialog;
import com.gc.service.notification.EmailNotificationsSender;
import com.gc.service.notification.SmsNotificationsSender;
import com.gc.vo.Notification;
import com.gc.vo.conf.SingleWindowLogin;

public class NotificationPanelListener implements TableModelListener, ActionListener {

	public static final String SEND_NOTIFICATION_ACTION_COMMAND = "SEND_NOTIFICATIONS";

	public static final String SELECT_ALL_EMAILS_ACTION_COMMAND = "SELECT_ALL_EMAILS";

	public static final String SELECT_ALL_SMS_ACTION_COMMAND = "SELECT_ALL_SMS";

	private final List<? extends Notification> notifications;

	private final EmailNotificationsSender emailNotificationSender;

	private final SmsNotificationsSender smsNotificationSender;

	private final JFrame parentFrame;

	private final JTable table;

	private final NotificationTableColumnCheckboxDecider checkboxDecider;

	private final SingleWindowLogin singleWindowLogin;

	public NotificationPanelListener(JFrame parentFrame, List<? extends Notification> notifications,
			EmailNotificationsSender emailNotificationSender, SmsNotificationsSender smsNotificationSender,
			JTable table, NotificationTableColumnCheckboxDecider checkboxDecider, SingleWindowLogin singleWindowLogin) {
		this.notifications = notifications;
		this.emailNotificationSender = emailNotificationSender;
		this.smsNotificationSender = smsNotificationSender;
		this.parentFrame = parentFrame;
		this.table = table;
		this.checkboxDecider = checkboxDecider;
		this.singleWindowLogin = singleWindowLogin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SEND_NOTIFICATION_ACTION_COMMAND == e.getActionCommand()) {
			LoginDialog ld = null;
			if (singleWindowLogin.isEnabled()) {
				ld = new SingleWindowLoginDialog(parentFrame, singleWindowLogin);
			} else {
				ld = new SimpleLoginDialog(parentFrame);
			}
			if (ld.isSucceeded()) {
				sendNotifications(ld);
			}
		} else if (SELECT_ALL_EMAILS_ACTION_COMMAND == e.getActionCommand()) {
			JCheckBox ch = (JCheckBox) e.getSource();
			boolean isSelected = ch.isSelected();
			for (int i = 0; i < notifications.size(); i++) {
				Notification notification = notifications.get(i);
				if (notification.getMemberDetail().canSendEmail()) {
					table.setValueAt(isSelected, i, checkboxDecider.getEmailColumnNumber());
				}
			}
			table.repaint();
		} else if (SELECT_ALL_SMS_ACTION_COMMAND == e.getActionCommand()) {
			JCheckBox ch = (JCheckBox) e.getSource();
			boolean isSelected = ch.isSelected();
			for (int i = 0; i < notifications.size(); i++) {
				Notification notification = notifications.get(i);
				if (notification.getMemberDetail().canSendSMS()) {
					table.setValueAt(isSelected, i, checkboxDecider.getSmsColumnNumber());
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
		if (column == checkboxDecider.getEmailColumnNumber()) {
			changedNotification.setSendEmail(value);
		} else if (column == checkboxDecider.getSmsColumnNumber()) {
			changedNotification.setSendSms(value);
		}
	}

	private void sendNotifications(LoginDialog ld) {
		SendNotificationsProgress snp = new SendNotificationsProgress(parentFrame);
		ProgressListener emailProgressListener = snp.createProgressListener("Sent Emails");
		ProgressListener smsProgressListener = snp.createProgressListener("Sent SMS");
		String emailUser = ld.getEmailUsername();
		String emailPassword = ld.getEmailPassword();
		String smsUser = ld.getSmsUsername();
		String smsPassword = ld.getSmsPassword();
		Runnable emailSenderRunnable = () -> {
			emailNotificationSender.send(emailUser, emailPassword, notifications, emailProgressListener);
			smsNotificationSender.send(smsUser, smsPassword, notifications, smsProgressListener);
		};
		snp.addProcessOnLoad(emailSenderRunnable);
		snp.init();
		snp.setVisible(true);
	}

}
