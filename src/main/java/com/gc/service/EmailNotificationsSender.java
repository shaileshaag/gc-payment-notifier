package com.gc.service;

import java.util.List;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.util.GcEmailSender;
import com.gc.util.GcEmailSender.SessionEmailsHolder;
import com.gc.vo.EmailNotificationProperties;
import com.gc.vo.EmailNotificationProperties.BodyProperties;
import com.gc.vo.Notification;

public class EmailNotificationsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationsSender.class);

	private final EmailNotificationProperties emailNotificationProperties;

	private final GcEmailSender emailSender;

	public EmailNotificationsSender(EmailNotificationProperties emailNotificationProperties,
			GcEmailSender emailSender) {
		this.emailNotificationProperties = emailNotificationProperties;
		this.emailSender = emailSender;
	}

	public void send(String username, String password, List<? extends Notification> notifications,
			ProgressListener progressListener) {
		progressListener.intimate("Preparing email notifications");
		SessionEmailsHolder emailsHolder = emailSender.getEmailsHolder(progressListener, username, password);
		for (Notification n : notifications) {
			if (n.isSendEmail()) {
				String body = getBody(n);
				emailsHolder.addMessage(n.getMemberDetail().getEmail(), emailNotificationProperties.getSubject(), body);
			}
		}
		if (emailNotificationProperties.isEnabled()) {
			emailsHolder.send();
		} else {
			progressListener.intimate(String.format("Prepared %s messages. Sending email has been disabled",
					emailsHolder.getMessagesCount()));
			LOGGER.info("Sending email has been disabled");
		}
		progressListener.markComplete();
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
