package com.gc.vo;

public class SmsNotificationProperties {

	private boolean enabled;

	private String textLocalUrl;

	private String apiKey;

	private String sender;

	private BodyProperties body;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public BodyProperties getBody() {
		return body;
	}

	public void setBody(BodyProperties body) {
		this.body = body;
	}

	public String getTextLocalUrl() {
		return textLocalUrl;
	}

	public void setTextLocalUrl(String textLocalUrl) {
		this.textLocalUrl = textLocalUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public static class BodyProperties {

		private String receivedNotification;

		private String thankYouNotification;

		public String getReceivedNotification() {
			return receivedNotification;
		}

		public void setReceivedNotification(String receivedNotification) {
			this.receivedNotification = receivedNotification;
		}

		public String getThankYouNotification() {
			return thankYouNotification;
		}

		public void setThankYouNotification(String thankYouNotification) {
			this.thankYouNotification = thankYouNotification;
		}

	}

}
