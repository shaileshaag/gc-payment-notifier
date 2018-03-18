package com.gc.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NotificationEntity {

	@Id
	private Long id;

	private String notif_type;

	private String to_flat;

	private String notif_content;

	private String notif_response;

	private Timestamp sent_on;

	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNotif_type() {
		return notif_type;
	}

	public void setNotif_type(String notif_type) {
		this.notif_type = notif_type;
	}

	public String getTo_flat() {
		return to_flat;
	}

	public void setTo_flat(String to_flat) {
		this.to_flat = to_flat;
	}

	public String getNotif_content() {
		return notif_content;
	}

	public void setNotif_content(String notif_content) {
		this.notif_content = notif_content;
	}

	public String getNotif_response() {
		return notif_response;
	}

	public void setNotif_response(String notif_response) {
		this.notif_response = notif_response;
	}

	public Timestamp getSent_on() {
		return sent_on;
	}

	public void setSent_on(Timestamp sent_on) {
		this.sent_on = sent_on;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationEntity [id=").append(id).append(", notif_type=").append(notif_type)
				.append(", to_flat=").append(to_flat).append(", notif_content=").append(notif_content)
				.append(", notif_response=").append(notif_response).append(", sent_on=").append(sent_on)
				.append(", status=").append(status).append("]");
		return builder.toString();
	}

}
