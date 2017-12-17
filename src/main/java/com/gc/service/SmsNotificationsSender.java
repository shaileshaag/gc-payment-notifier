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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.component.listener.ProgressListener;
import com.gc.util.Formats;
import com.gc.vo.BusinessMantraResponse;
import com.gc.vo.Notification;
import com.gc.vo.SmsNotificationProperties;
import com.gc.vo.SmsNotificationProperties.BodyProperties;

public class SmsNotificationsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationsSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	private static final String USERNAME_PARAM_NAME = "user";

	private static final String PASSWORD_PARAM_NAME = "password";

	private static final String MESSAGE_PARAM_NAME = "msg";

	private static final String SENDER_PARAM_NAME = "sid";

	private static final String NUMBERS_PARAM_NAME = "msisdn";

	private static final String FLASH_PARAM_NAME = "fl";

	private static final String GWID_PARAM_NAME = "gwid";

	private final SmsNotificationProperties smsNotificationProperties;

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	public SmsNotificationsSender(SmsNotificationProperties smsNotificationProperties, RestTemplate restTemplate) {
		this.smsNotificationProperties = smsNotificationProperties;
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper();
	}

	public void send(String username, String password, List<? extends Notification> notifications,
			ProgressListener progressListener) {
		progressListener.intimate("Preparing SMS notifications");
		List<Map<String, String>> messages = new ArrayList<>();
		for (Notification n : notifications) {
			if (n.isSendSms()) {
				try {
					String msg = buildSms(n);
					Map<String, String> uriVariables = new HashMap<>();
					uriVariables.put(USERNAME_PARAM_NAME, username);
					uriVariables.put(NUMBERS_PARAM_NAME, n.getMemberDetail().getMobile());
					uriVariables.put(SENDER_PARAM_NAME, smsNotificationProperties.getSender());
					uriVariables.put(MESSAGE_PARAM_NAME, msg);
					uriVariables.put(PASSWORD_PARAM_NAME, password);
					uriVariables.put(FLASH_PARAM_NAME, smsNotificationProperties.getFlashParam());
					uriVariables.put(GWID_PARAM_NAME, smsNotificationProperties.getGwidParam());
					messages.add(uriVariables);
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("Could not build message for notification: {}", n);
				}
			}
		}
		if (smsNotificationProperties.isEnabled()) {
			if (messages.isEmpty()) {
				progressListener.intimate("No SMS selected");
			} else {
				progressListener.setProgressMax(messages.size());
				progressListener
						.intimate(String.format("%d SMS notifications ready to be sent. Sending...", messages.size()));
				sendSms(progressListener, messages);
			}
		} else {
			progressListener.intimate(String.format("Prepared %s messages. Sending SMS has been disabled", messages));
			LOGGER.info("Sending SMS has been disabled");
		}
		progressListener.markComplete();
	}

	private void sendSms(ProgressListener progressListener, List<Map<String, String>> messages) {
		int sendCounter = 0;
		for (Map<String, String> smm : messages) {
			sendCounter++;
			String url = buildUrl(smm);
			SENT_NOTIF_LOGGER.info("Invoking URL {}", url);
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
				SENT_NOTIF_LOGGER.info("Sent SMS: {}\nResponse: {}", smm, response);
				BusinessMantraResponse resp = objectMapper.readValue(response.getBody(), BusinessMantraResponse.class);
				String smsRespProgress = null;
				if (resp.getMessageData().isEmpty() || resp.getMessageData().get(0).getMessageParts().isEmpty()) {
					smsRespProgress = String.format(
							"=====\nSMS sent to %s\nSMS Message:  %s\nStatus: %s (%d of %d)\n=====",
							smm.get(NUMBERS_PARAM_NAME), smm.get(MESSAGE_PARAM_NAME), resp.getErrorMessage(),
							sendCounter, messages.size());
				} else if (resp.getMessageData().size() == 1) {
					String msgId = resp.getMessageData().get(0).getMessageParts().get(0).getMsgId();
					smsRespProgress = String.format(
							"=====\nSMS sent to %s (%d of %d)\nSMS Message:  %s\nStatus: %s\nMessage Id: %s\n=====",
							smm.get(NUMBERS_PARAM_NAME), sendCounter, messages.size(), smm.get(MESSAGE_PARAM_NAME),
							resp.getErrorMessage(), msgId);
				}
				progressListener.sent(smsRespProgress);
			} catch (Exception e) {
				progressListener.sent(String.format("Failed to send SMS to %s (%d or %d)", smm.get(NUMBERS_PARAM_NAME),
						sendCounter, messages.size()));
				SENT_NOTIF_LOGGER.error("Error while invoking SMS URL: {}", url, e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		}
	}

	private String buildSms(Notification n) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		BodyProperties body = smsNotificationProperties.getBody();
		StrSubstitutor replaceTemplate = Formats.prepareReplaceTemplate(n);
		String recNot = replaceTemplate.replace(body.getReceivedNotification());
		sb.append(recNot);
		String thankYouNot = replaceTemplate.replace(body.getThankYouNotification());
		sb.append(" ").append(thankYouNot);
		return sb.toString();
	}

	private String buildUrl(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(smsNotificationProperties.getBusinessMantraUrl());
		sb.append("?");
		boolean firstAppended = false;
		Iterator<Map.Entry<String, String>> paramsIter = params.entrySet().iterator();
		while (paramsIter.hasNext()) {
			Map.Entry<String, String> e = paramsIter.next();
			if (firstAppended) {
				sb.append("&");
			} else {
				firstAppended = true;
			}
			sb.append(e.getKey()).append("=").append(e.getValue());
		}
		return sb.toString();
	}

}
