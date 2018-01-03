package com.gc.vo.conf;

public class EmailNotificationProperties {

	private boolean enabled;

	private String replyTo;

	private String ccTo;

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

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getCcTo() {
		return ccTo;
	}

	public void setCcTo(String ccTo) {
		this.ccTo = ccTo;
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
