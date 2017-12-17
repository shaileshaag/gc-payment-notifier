package com.gc.vo;

import java.util.List;

public class TextLocalResponse {

	private long balance;

	private long batch_id;

	private long cost;

	private long num_messages;

	private String status;

	private Message message;

	private List<NumberResponse> messages;

	private List<NumberErrorWarn> errors;

	private List<NumberErrorWarn> warnings;

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(long batch_id) {
		this.batch_id = batch_id;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public long getNum_messages() {
		return num_messages;
	}

	public void setNum_messages(long num_messages) {
		this.num_messages = num_messages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<NumberResponse> getMessages() {
		return messages;
	}

	public void setMessages(List<NumberResponse> messages) {
		this.messages = messages;
	}

	public List<NumberErrorWarn> getErrors() {
		return errors;
	}

	public void setErrors(List<NumberErrorWarn> errors) {
		this.errors = errors;
	}

	public List<NumberErrorWarn> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<NumberErrorWarn> warnings) {
		this.warnings = warnings;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextLocalResponse [balance=").append(balance).append(", batch_id=").append(batch_id)
				.append(", cost=").append(cost).append(", num_messages=").append(num_messages).append(", status=")
				.append(status).append(", message=").append(message).append(", messages=").append(messages)
				.append(", errors=").append(errors).append(", warnings=").append(warnings).append("]");
		return builder.toString();
	}

	public static class Message {

		private long num_parts;

		private String sender;

		private String content;

		public long getNum_parts() {
			return num_parts;
		}

		public void setNum_parts(long num_parts) {
			this.num_parts = num_parts;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(" [num_parts=").append(num_parts).append(", sender=").append(sender).append(", content=")
					.append(content).append("]");
			return builder.toString();
		}

	}

	private static class NumberResponse {

		private String id;

		private long recipient;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getRecipient() {
			return recipient;
		}

		public void setRecipient(long recipient) {
			this.recipient = recipient;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(" [id=").append(id).append(", recipient=").append(recipient).append("]");
			return builder.toString();
		}

	}

	private static class NumberErrorWarn {

		private long code;

		private String message;

		public long getCode() {
			return code;
		}

		public void setCode(long code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(" [code=").append(code).append(", message=").append(message).append("]");
			return builder.toString();
		}

	}

}
