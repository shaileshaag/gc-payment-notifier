package com.gc.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.vo.EmailNotificationProperties;
import com.gc.vo.EmailNotificationProperties.BodyProperties;
import com.gc.vo.Notification;

public class EmailNotificationsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationsSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	@Resource
	private final EmailNotificationProperties emailNotificationProperties;

	@Resource
	private final JavaMailSenderImpl javaMailSender;

	public EmailNotificationsSender(EmailNotificationProperties emailNotificationProperties,
			JavaMailSenderImpl javaMailSender) {
		this.emailNotificationProperties = emailNotificationProperties;
		this.javaMailSender = javaMailSender;
	}

	public void send(String username, String password, List<? extends Notification> notifications,
			ProgressListener progressListener) {
		List<SimpleMailMessage> messages = new ArrayList<>();
		String fromEmail = username + "@gmail.com";
		progressListener.intimate("Preparing email notifications");
		for (Notification n : notifications) {
			if (n.isSendEmail()) {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setSubject(emailNotificationProperties.getSubject());
				message.setFrom(fromEmail);
				message.setTo(n.getMemberDetail().getEmail());
				String body = getBody(n);
				message.setText(body);
				messages.add(message);
			}
		}
		if (emailNotificationProperties.isEnabled()) {
			if (messages.isEmpty()) {
				progressListener.intimate("No emails selected");
			} else {
				progressListener.intimate(
						String.format("%d Email notifications ready to be sent. Sending...", messages.size()));
				javaMailSender.setUsername(fromEmail);
				javaMailSender.setPassword(password);
				progressListener.setProgressMax(messages.size());
				sendEmails(progressListener, messages);
				javaMailSender.setUsername(null);
				javaMailSender.setPassword(null);
			}
		} else {
			progressListener.intimate(String.format("Prepared %s messages. Sending email has been disabled", messages));
			LOGGER.info("Sending email has been disabled");
		}
		progressListener.markComplete();
	}

	private void sendEmails(ProgressListener progressListener, List<SimpleMailMessage> messages) {
		int sendCounter = 0;
		for (SimpleMailMessage smm : messages) {
			try {
				sendCounter++;
				javaMailSender.send(smm);
				SENT_NOTIF_LOGGER.info("Sent email: {}", smm);
				progressListener
						.sent(String.format("=====\nEmail sent to %s (%d or %d) with message:\n%s\n=====",
								smm.getTo()[0], sendCounter, messages.size(), smm.getText()));
			} catch (Exception e) {
				progressListener.sent(String.format("Failed to send email to %s (%d or %d): %s", smm.getTo()[0],
						sendCounter, messages.size(), e.getCause().getMessage()));
				LOGGER.error("Error while sending email: {}", smm, e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		}
	}

	private String getBody(Notification n) {
		StringBuilder sb = new StringBuilder();
		BodyProperties body = emailNotificationProperties.getBody();
		StrSubstitutor replaceTemplate = Formats.prepareReplaceTemplate(n);
		String recNot = body.getReceivedNotification();
		sb.append(recNot);
		String thankYouNot = body.getThankYouNotification();
		sb.append("\n\n").append(thankYouNot);
		return replaceTemplate.replace(sb.toString());
	}

}
