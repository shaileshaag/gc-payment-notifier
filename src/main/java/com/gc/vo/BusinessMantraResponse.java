package com.gc.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessMantraResponse {

	@JsonProperty
	private String ErrorCode;

	@JsonProperty
	private String ErrorMessage;

	@JsonProperty
	private String JobId;

	@JsonProperty
	private List<MessageData> MessageData;

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public String getJobId() {
		return JobId;
	}

	public void setJobId(String jobId) {
		JobId = jobId;
	}

	public List<MessageData> getMessageData() {
		return MessageData;
	}

	public void setMessageData(List<MessageData> messageData) {
		MessageData = messageData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessMantraResponse [ErrorCode=").append(ErrorCode).append(", ErrorMessage=")
				.append(ErrorMessage).append(", JobId=").append(JobId).append(", MessageData=").append(MessageData)
				.append("]");
		return builder.toString();
	}

	public static class MessageData {

		@JsonProperty
		private String Number;

		@JsonProperty
		private List<MessagePart> MessageParts;

		public String getNumber() {
			return Number;
		}

		public void setNumber(String number) {
			Number = number;
		}

		public List<MessagePart> getMessageParts() {
			return MessageParts;
		}

		public void setMessageParts(List<MessagePart> messageParts) {
			MessageParts = messageParts;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(" [Number=").append(Number).append(", MessageParts=").append(MessageParts).append("]");
			return builder.toString();
		}

	}

	public static class MessagePart {

		@JsonProperty
		private String MsgId;

		@JsonProperty
		private long PartId;

		@JsonProperty
		private String Text;

		public String getMsgId() {
			return MsgId;
		}

		public void setMsgId(String msgId) {
			MsgId = msgId;
		}

		public long getPartId() {
			return PartId;
		}

		public void setPartId(long partId) {
			PartId = partId;
		}

		public String getText() {
			return Text;
		}

		public void setText(String text) {
			Text = text;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(" [MsgId=").append(MsgId).append(", PartId=").append(PartId).append(", Text=").append(Text)
					.append("]");
			return builder.toString();
		}

	}
}
