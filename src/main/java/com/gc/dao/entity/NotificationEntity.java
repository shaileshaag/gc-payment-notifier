package com.gc.dao.entity;

import java.sql.Clob;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NotificationEntity {

	@Id
	private Long id;

	private String notif_type;

	private String to_flat;

	private Clob notif_content;

	private Clob notif_response;

	private Timestamp sent_on;

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

	public Clob getNotif_content() {
		return notif_content;
	}

	public void setNotif_content(Clob notif_content) {
		this.notif_content = notif_content;
	}

	public Clob getNotif_response() {
		return notif_response;
	}

	public void setNotif_response(Clob notif_response) {
		this.notif_response = notif_response;
	}

	public Timestamp getSent_on() {
		return sent_on;
	}

	public void setSent_on(Timestamp sent_on) {
		this.sent_on = sent_on;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationEntity [id=").append(id).append(", notif_type=").append(notif_type)
				.append(", to_flat=").append(to_flat).append(", notif_content=").append(notif_content)
				.append(", notif_response=").append(notif_response).append(", sent_on=").append(sent_on).append("]");
		return builder.toString();
	}

}
