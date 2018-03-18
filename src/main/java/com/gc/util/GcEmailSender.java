package com.gc.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.gc.component.listener.ProgressListener;
import com.gc.dao.NotificationRepository;

public class GcEmailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(GcEmailSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	private final JavaMailSenderImpl javaMailSender;

	private NotificationRepository notificationRepository;

	public GcEmailSender(JavaMailSenderImpl javaMailSender, NotificationRepository notificationRepository) {
		this.javaMailSender = javaMailSender;
	}

	public SessionEmailsHolder getEmailsHolder(ProgressListener progressListener, String emailId, String password) {
		return new SessionEmailsHolder(progressListener, emailId, password);
	}

	private void sendEmails(String emailId, String password, ProgressListener progressListener,
			List<SimpleMailMessage> messages) {
		progressListener.setProgressMax(messages.size());
		setUsernamePassword(emailId, password);

		progressListener
				.intimate(String.format("%d Email notifications ready to be sent. Sending...", messages.size()));

		int sendCounter = 0;
		for (SimpleMailMessage smm : messages) {
			sendCounter++;
			try {
				SENT_NOTIF_LOGGER.info("Seding email: {}", smm);
				javaMailSender.send(smm);
				SENT_NOTIF_LOGGER.info("Sent email: {}", smm);
				progressListener.sent(String.format("=====\nEmail sent to %s (%d or %d) with message:\n%s\n=====",
						smm.getTo()[0], sendCounter, messages.size(), smm.getText()));
			} catch (Exception e) {
				progressListener.sent(String.format("Failed to send email to %s (%d or %d): %s", smm.getTo()[0],
						sendCounter, messages.size(), e.getCause().getMessage()));
				SENT_NOTIF_LOGGER.error("Error while sending email: {}", smm, e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		}

		setUsernamePassword(null, null);
	}

	private void setUsernamePassword(String emailId, String password) {
		javaMailSender.setUsername(emailId);
		javaMailSender.setPassword(password);
	}

	public class SessionEmailsHolder {

		private final ProgressListener progressListener;

		private final List<SimpleMailMessage> messages;

		private final String emailId;

		private final String password;

		private SessionEmailsHolder(ProgressListener progressListener, String username, String password) {
			this.progressListener = progressListener;
			this.emailId = username + "@gmail.com";
			this.password = password;
			this.messages = new ArrayList<>();
		}

		public int getMessagesCount() {
			return messages.size();
		}

		public void addMessage(String to, String replyTo, String ccTo, String subject, String body) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject(subject);
			message.setFrom(emailId);
			message.setReplyTo(replyTo);
			message.setCc(ccTo);
			message.setTo(to);
			message.setText(body);
			messages.add(message);
		}

		public void send() {
			if (messages.isEmpty()) {
				progressListener.intimate("No emails selected");
			} else {
				sendEmails(emailId, password, progressListener, messages);
			}
		}

		public String getPrintableMessages() {
			StringBuilder sb = new StringBuilder();
			for (SimpleMailMessage smm : messages) {
				sb.append("====").append(smm.toString()).append("====\n");
			}
			return sb.toString();
		}

	}

}
