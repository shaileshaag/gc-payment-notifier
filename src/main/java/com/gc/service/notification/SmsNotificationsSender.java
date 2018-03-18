package com.gc.service.notification;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.util.GcSmsSender;
import com.gc.util.GcSmsSender.SessionSmsHolder;
import com.gc.vo.Notification;
import com.gc.vo.conf.SmsNotificationProperties;
import com.gc.vo.conf.SmsNotificationProperties.BodyProperties;

public class SmsNotificationsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationsSender.class);

	private final SmsNotificationProperties smsNotificationProperties;

	private final GcSmsSender smsSender;

	public SmsNotificationsSender(SmsNotificationProperties smsNotificationProperties, GcSmsSender smsSender) {
		this.smsNotificationProperties = smsNotificationProperties;
		this.smsSender = smsSender;
	}

	public void send(String username, String password, List<? extends Notification> notifications,
			ProgressListener progressListener) {
		progressListener.intimate("Preparing SMS notifications");
		SessionSmsHolder smsHolder = smsSender.getSmsHolder(progressListener, username, password);
		for (Notification n : notifications) {
			if (n.isSendSms()) {
				try {
					String msg = buildSmsMessage(n);
					smsHolder.addMessage(n.getMemberDetail().getFlatNo(), n.getMemberDetail().getMobile(), msg);
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("Could not build message for notification: {}", n);
				}
			}
		}
		if (smsNotificationProperties.isEnabled()) {
			smsHolder.send();
		} else {
			progressListener.intimate(
					String.format("Prepared %s messages. Sending SMS has been disabled", smsHolder.getMessagesCount()));
			LOGGER.info("Sending SMS has been disabled");
			LOGGER.info(smsHolder.getPrintableMessages());
		}
		progressListener.markComplete();
	}

	private String buildSmsMessage(Notification n) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		BodyProperties body = smsNotificationProperties.getBody();
		StrSubstitutor replaceTemplate = Formats.prepareReplaceTemplate(n);
		String recNot = replaceTemplate.replace(body.getReceivedNotification());
		sb.append(recNot);
		String thankYouNot = replaceTemplate.replace(body.getThankYouNotification());
		sb.append(" ").append(thankYouNot);
		return sb.toString();
	}

}
