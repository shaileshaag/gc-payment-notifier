package com.gc.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.component.listener.ProgressListener;
import com.gc.dao.NotificationRepository;
import com.gc.dao.entity.NotificationEntity;
import com.gc.vo.conf.SmsNotificationProperties;
import com.gc.vo.partner.BusinessMantraResponse;

public class GcSmsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(GcSmsSender.class);

	private static final Logger SENT_NOTIF_LOGGER = LoggerFactory.getLogger("notifications-logger");

	public static final String USERNAME_PARAM_NAME = "user";

	public static final String PASSWORD_PARAM_NAME = "password";

	public static final String MESSAGE_PARAM_NAME = "msg";

	public static final String SENDER_PARAM_NAME = "sid";

	public static final String NUMBERS_PARAM_NAME = "msisdn";

	private final RestTemplate restTemplate;

	private final NotificationRepository notificationRepository;

	private final ObjectMapper objectMapper;

	private final String parameterizedUrl;

	private final String sender;

	public GcSmsSender(RestTemplate restTemplate, SmsNotificationProperties smsNotificationProperties,
			NotificationRepository notificationRepository) {
		this.restTemplate = restTemplate;
		this.notificationRepository = notificationRepository;
		this.parameterizedUrl = smsNotificationProperties.getBusinessMantraUrl();
		this.sender = smsNotificationProperties.getSender();
		this.objectMapper = new ObjectMapper();
	}

	public SessionSmsHolder getSmsHolder(ProgressListener progressListener, String username, String password) {
		return new SessionSmsHolder(progressListener, username, password);
	}

	private void sendSms(ProgressListener progressListener, Map<String, Map<String, String>> messages) {
		progressListener.setProgressMax(messages.size());
		AtomicInteger sendCounterAi = new AtomicInteger();
		messages.entrySet().forEach(entry -> {
			String flatNo = entry.getKey();
			Map<String, String> smm = entry.getValue();
			int sendCounter = sendCounterAi.incrementAndGet();
			BusinessMantraResponse resp = null;
			String status = null;
			SENT_NOTIF_LOGGER.info("Invoking URL {} with params {}", parameterizedUrl, smm);
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(parameterizedUrl, null, String.class, smm);
				SENT_NOTIF_LOGGER.info("Sent SMS: {}\nResponse: {}", smm, response);
				resp = objectMapper.readValue(response.getBody(), BusinessMantraResponse.class);
				String smsRespProgress = null;
				if (resp.getMessageData().isEmpty() || resp.getMessageData().get(0).getMessageParts().isEmpty()) {
					smsRespProgress = String.format(
							"=====\nSMS sent to %s\nSMS Message:  %s\nStatus: %s (%d of %d)\n=====",
							smm.get(NUMBERS_PARAM_NAME), smm.get(MESSAGE_PARAM_NAME), resp.getErrorMessage(),
							sendCounter, messages.size());
					status = Constants.STATUS_FAILURE;
				} else if (resp.getMessageData().size() == 1) {
					String msgId = resp.getMessageData().get(0).getMessageParts().get(0).getMsgId();
					smsRespProgress = String.format(
							"=====\nSMS sent to %s (%d of %d)\nSMS Message:  %s\nStatus: %s\nMessage Id: %s\n=====",
							smm.get(NUMBERS_PARAM_NAME), sendCounter, messages.size(), smm.get(MESSAGE_PARAM_NAME),
							resp.getErrorMessage(), msgId);
					status = Constants.STATUS_SUCCESSFUL;
				}
				progressListener.sent(smsRespProgress);
			} catch (Exception e) {
				progressListener.sent(String.format("Failed to send SMS to %s (%d or %d)", smm.get(NUMBERS_PARAM_NAME),
						sendCounter, messages.size()));
				SENT_NOTIF_LOGGER.error("Error while invoking SMS URL: {} with params {}", parameterizedUrl, smm, e);
				status = Constants.STATUS_FAILURE;
			}
			persistNotification(flatNo, smm, status, resp);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				LOGGER.error("Sleeping thread interrupted", e);
			}
		});
	}

	private void persistNotification(String flatNo, Map<String, String> messageParams, String status,
			BusinessMantraResponse response) {
		try {
			String messageString = objectMapper.writeValueAsString(messageParams);
			String responseString = objectMapper.writeValueAsString(response);
			NotificationEntity ne = new NotificationEntity();
			ne.setNotif_content(messageString);
			ne.setNotif_response(responseString);
			ne.setNotif_type("SMS");
			ne.setSent_on(new Timestamp(new Date().getTime()));
			ne.setStatus(status);
			ne.setTo_flat(flatNo);
			notificationRepository.save(ne);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while persisting SMS notification \"flatNo\": {}, \"messageParams\": {}, \"status\": {}, \"response\": {} to storage",
					flatNo, messageParams, status, response, e);
		}
	}

	public class SessionSmsHolder {

		private final ProgressListener progressListener;

		private final Map<String, Map<String, String>> messages;

		private final String username;

		private final String password;

		private SessionSmsHolder(ProgressListener progressListener, String username, String password) {
			this.progressListener = progressListener;
			this.username = username;
			this.password = password;
			this.messages = new HashMap<>();
		}

		public int getMessagesCount() {
			return messages.size();
		}

		public void addMessage(String flatNo, String mobileNo, String message) {
			Map<String, String> duplicateParams = new HashMap<>();
			duplicateParams.put(NUMBERS_PARAM_NAME, mobileNo);
			duplicateParams.put(MESSAGE_PARAM_NAME, message);
			duplicateParams.put(USERNAME_PARAM_NAME, username);
			duplicateParams.put(PASSWORD_PARAM_NAME, password);
			duplicateParams.put(SENDER_PARAM_NAME, sender);
			messages.put(flatNo, duplicateParams);
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
			messages.entrySet().forEach(entry -> {
				String flatNo = entry.getKey();
				Map<String, String> smm = entry.getValue();
				sb.append("====").append(flatNo).append("-->").append(smm).append("====\n");
			});
			return sb.toString();
		}

	}

}
