package com.gc.vo;

public class EmailNotificationProperties {

	private boolean enabled;

	private String subject;

	private BodyProperties body;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public BodyProperties getBody() {
		return body;
	}

	public void setBody(BodyProperties body) {
		this.body = body;
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
