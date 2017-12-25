package com.gc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.component.listener.ProgressListener;
import com.gc.vo.BusinessMantraResponse;
import com.gc.vo.SmsNotificationProperties;

public class GcSmsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(GcSmsSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	public static final String USERNAME_PARAM_NAME = "user";

	public static final String PASSWORD_PARAM_NAME = "password";

	public static final String MESSAGE_PARAM_NAME = "msg";

	public static final String SENDER_PARAM_NAME = "sid";

	public static final String NUMBERS_PARAM_NAME = "msisdn";

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	private final String parameterizedUrl;

	private final String sender;

	public GcSmsSender(RestTemplate restTemplate, SmsNotificationProperties smsNotificationProperties) {
		this.restTemplate = restTemplate;
		this.parameterizedUrl = smsNotificationProperties.getBusinessMantraUrl();
		this.sender = smsNotificationProperties.getSender();
		this.objectMapper = new ObjectMapper();
	}

	public SessionSmsHolder getSmsHolder(ProgressListener progressListener, String username, String password) {
		return new SessionSmsHolder(progressListener, username, password);
	}

	private void sendSms(ProgressListener progressListener, List<Map<String, String>> messages) {
		progressListener.setProgressMax(messages.size());
		int sendCounter = 0;
		for (Map<String, String> smm : messages) {
			sendCounter++;
			SENT_NOTIF_LOGGER.info("Invoking URL {} with params {}", parameterizedUrl, smm);
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(parameterizedUrl, null, String.class, smm);
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
				SENT_NOTIF_LOGGER.error("Error while invoking SMS URL: {} with params {}", parameterizedUrl, smm, e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		}
	}

	public class SessionSmsHolder {

		private final ProgressListener progressListener;

		private final List<Map<String, String>> messages;

		private final String username;

		private final String password;

		private SessionSmsHolder(ProgressListener progressListener, String username, String password) {
			this.progressListener = progressListener;
			this.username = username;
			this.password = password;
			this.messages = new ArrayList<>();
		}

		public int getMessagesCount() {
			return messages.size();
		}

		public void addMessage(String mobileNo, String message) {
			Map<String, String> duplicateParams = new HashMap<>();
			duplicateParams.put(NUMBERS_PARAM_NAME, mobileNo);
			duplicateParams.put(MESSAGE_PARAM_NAME, message);
			duplicateParams.put(USERNAME_PARAM_NAME, username);
			duplicateParams.put(PASSWORD_PARAM_NAME, password);
			duplicateParams.put(SENDER_PARAM_NAME, sender);
			messages.add(duplicateParams);
		}

		public void send() {
			if (messages.isEmpty()) {
				progressListener.intimate("No SMS selected");
			} else {
				sendSms(progressListener, messages);
			}
		}

		public String getPrintableMessages() {
			StringBuilder sb = new StringBuilder();
			for (Map<String, String> smm : messages) {
				sb.append("====").append(smm).append("====\n");
			}
			return sb.toString();
		}

	}

}
