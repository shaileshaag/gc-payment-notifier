package com.gc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.gc.component.listener.ProgressListener;
import com.gc.dao.NotificationRepositoryDao;

public class GcEmailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(GcEmailSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	private final JavaMailSenderImpl javaMailSender;

	private final NotificationRepositoryDao notificationRepositoryDao;

	public GcEmailSender(JavaMailSenderImpl javaMailSender, NotificationRepositoryDao notificationRepositoryHelper) {
		this.javaMailSender = javaMailSender;
		this.notificationRepositoryDao = notificationRepositoryHelper;
	}

	public SessionEmailsHolder getEmailsHolder(ProgressListener progressListener, String emailId, String password) {
		return new SessionEmailsHolder(progressListener, emailId, password);
	}

	private void sendEmails(String emailId, String password, ProgressListener progressListener,
			Map<String, SimpleMailMessage> messages) {
		progressListener.setProgressMax(messages.size());
		setUsernamePassword(emailId, password);

		progressListener
				.intimate(String.format("%d Email notifications ready to be sent. Sending...", messages.size()));

		AtomicInteger sendCounterAi = new AtomicInteger();
		messages.entrySet().forEach(entry -> {
			String flatNo = entry.getKey();
			SimpleMailMessage smm = entry.getValue();
			String status = null;
			int sendCounter = sendCounterAi.incrementAndGet();
			try {
				SENT_NOTIF_LOGGER.info("Seding email: {}", smm);
				javaMailSender.send(smm);
				SENT_NOTIF_LOGGER.info("Sent email: {}", smm);
				progressListener.sent(String.format("=====\nEmail sent to %s (%d or %d) with message:\n%s\n=====",
						smm.getTo()[0], sendCounter, messages.size(), smm.getText()));
				status = Constants.STATUS_SUCCESSFUL;
			} catch (Exception e) {
				progressListener.sent(String.format("Failed to send email to %s (%d or %d): %s", smm.getTo()[0],
						sendCounter, messages.size(), e.getCause().getMessage()));
				SENT_NOTIF_LOGGER.error("Error while sending email: {}", smm, e);
				status = Constants.STATUS_FAILURE;
			}
			try {
				notificationRepositoryDao.persistNotification(flatNo, smm, status);
			} catch (Exception e) {
				LOGGER.error(
						"Exception while persisting EMAIL notification \"flatNo\": {}, \"message\": {}, \"status\": {} to storage",
						flatNo, smm, status, e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		});

		setUsernamePassword(null, null);
	}

	private void setUsernamePassword(String emailId, String password) {
		javaMailSender.setUsername(emailId);
		javaMailSender.setPassword(password);
	}

	public class SessionEmailsHolder {

		private final ProgressListener progressListener;

		private final Map<String, SimpleMailMessage> messages;

		private final String emailId;

		private final String password;

		private SessionEmailsHolder(ProgressListener progressListener, String username, String password) {
			this.progressListener = progressListener;
			this.emailId = username + "@gmail.com";
			this.password = password;
			this.messages = new HashMap<>();
		}

		public int getMessagesCount() {
			return messages.size();
		}

		public void addMessage(String flatNo, String to, String replyTo, String ccTo, String subject, String body) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject(subject);
			message.setFrom(emailId);
			message.setReplyTo(replyTo);
			message.setCc(ccTo);
			message.setTo(to);
			message.setText(body);
			messages.put(flatNo, message);
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
			messages.entrySet().forEach(entry -> {
				String flatNo = entry.getKey();
				SimpleMailMessage smm = entry.getValue();
				sb.append("====").append(flatNo).append("-->").append(smm.toString()).append("====\n");
			});
			return sb.toString();
		}

	}

}
