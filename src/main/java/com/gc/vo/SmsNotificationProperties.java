package com.gc.vo;

public class SmsNotificationProperties {

	private boolean enabled;

	private String businessMantraUrl;

	private String sender;

	private String flashParam;
	
	private String gwidParam;

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

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBusinessMantraUrl() {
		return businessMantraUrl;
	}

	public void setBusinessMantraUrl(String businessMantraUrl) {
		this.businessMantraUrl = businessMantraUrl;
	}

	public String getFlashParam() {
		return flashParam;
	}

	public void setFlashParam(String flashParam) {
		this.flashParam = flashParam;
	}

	public String getGwidParam() {
		return gwidParam;
	}

	public void setGwidParam(String gwidParam) {
		this.gwidParam = gwidParam;
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
