package com.gc.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.vo.Notification;
import com.gc.vo.SmsNotificationProperties;
import com.gc.vo.SmsNotificationProperties.BodyProperties;

public class SmsNotificationsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationsSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	private static final String APIKEY_PARAM_NAME = "apikey";

	private static final String MESSAGE_PARAM_NAME = "message";

	private static final String SENDER_PARAM_NAME = "sender";

	private static final String NUMBERS_PARAM_NAME = "numbers";

	private final SmsNotificationProperties smsNotificationProperties;

	private final RestTemplate restTemplate;

	public SmsNotificationsSender(SmsNotificationProperties smsNotificationProperties, RestTemplate restTemplate) {
		this.smsNotificationProperties = smsNotificationProperties;
		this.restTemplate = restTemplate;
	}

	public void send(List<Notification> notifications, ProgressListener progressListener) {
		progressListener.intimate("Preparing SMS notifications");
		List<Map<String, String>> messages = new ArrayList<>();
		for (Notification n : notifications) {
			if (n.isSendSms()) {
				try {
					String msg = buildSms(n);
					Map<String, String> uriVariables = new HashMap<>();
					uriVariables.put(APIKEY_PARAM_NAME, smsNotificationProperties.getApiKey());
					uriVariables.put(NUMBERS_PARAM_NAME, n.getMemberDetail().getMobile());
					uriVariables.put(SENDER_PARAM_NAME, smsNotificationProperties.getSender());
					uriVariables.put(MESSAGE_PARAM_NAME, msg);
					messages.add(uriVariables);
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("Could not build message for notification: {}", n);
				}
			}
		}
		if (smsNotificationProperties.isEnabled()) {
			progressListener.setProgressMax(messages.size());
			progressListener
					.intimate(String.format("%d SMS notifications ready to be sent. Sending...", messages.size()));
			int sendCounter = 0;
			for (Map<String, String> smm : messages) {
				sendCounter++;
				String url = buildUrl(smm);
				SENT_NOTIF_LOGGER.info("Invoking URL {}", url);
				try {
					ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
					SENT_NOTIF_LOGGER.info("Sent SMS: {}\nResponse: ", smm, response);
					progressListener.sent(String.format("=====\nSMS sent to %s: %s (%d of %d)\n=====",
							smm.get(NUMBERS_PARAM_NAME), smm.get(MESSAGE_PARAM_NAME), sendCounter, messages.size()));
				} catch (Exception e) {
					progressListener.sent(String.format("Failed to send SMS to %s (%d or %d)",
							smm.get(NUMBERS_PARAM_NAME), sendCounter, messages.size()));
					SENT_NOTIF_LOGGER.error("Error while invoking SMS URL: {}", url, e);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					LOGGER.error("Sleeping thread interrupted", e);
				}
			}
		} else {
			progressListener.intimate("Sending SMS has been disabled");
			LOGGER.info("Sending SMS has been disabled");
		}
		progressListener.markComplete();
	}

	private String buildSms(Notification n) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		BodyProperties body = smsNotificationProperties.getBody();
		StrSubstitutor replaceTemplate = Formats.prepareReplaceTemplate(n);
		String recNot = replaceTemplate.replace(body.getReceivedNotification());
		recNot = UriUtils.encode(recNot, "UTF-8");
		sb.append(recNot);
		String thankYouNot = replaceTemplate.replace(body.getThankYouNotification());
		thankYouNot = UriUtils.encode(thankYouNot, "UTF-8");
		sb.append("%n").append(thankYouNot);
		return sb.toString();
	}

	private String buildUrl(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(smsNotificationProperties.getTextLocalUrl());
		sb.append("?");
		boolean firstAppended = false;
		Iterator<Map.Entry<String, String>> paramsIter = params.entrySet().iterator();
		while (paramsIter.hasNext()) {
			if (firstAppended) {
				sb.append("&");
			}
			Map.Entry<String, String> e = paramsIter.next();
			sb.append(e.getKey()).append("=").append(e.getValue());
		}
		return sb.toString();
	}

}
