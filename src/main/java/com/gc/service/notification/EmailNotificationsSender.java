package com.gc.service.notification;

import java.util.List;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.util.GcEmailSender;
import com.gc.util.GcEmailSender.SessionEmailsHolder;
import com.gc.vo.Notification;
import com.gc.vo.conf.EmailNotificationProperties;
import com.gc.vo.conf.EmailNotificationProperties.BodyProperties;

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
				String subject = getSubject(n);
				emailsHolder.addMessage(n.getMemberDetail().getFlatNo(), n.getMemberDetail().getEmail(), emailNotificationProperties.getReplyTo(),
						emailNotificationProperties.getCcTo(), subject, body);
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

	private String getSubject(Notification n) {
		StringBuilder sb = new StringBuilder();
		String subject = emailNotificationProperties.getSubject();
		StrSubstitutor replaceTemplate = Formats.prepareReplaceTemplate(n);
		sb.append(subject);
		return replaceTemplate.replace(sb.toString());
	}

}
