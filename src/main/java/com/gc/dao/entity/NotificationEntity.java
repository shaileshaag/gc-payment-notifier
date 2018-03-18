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

	private String address;

	private String remote_notif_id;

	private Clob notif_content;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemote_notif_id() {
		return remote_notif_id;
	}

	public void setRemote_notif_id(String remote_notif_id) {
		this.remote_notif_id = remote_notif_id;
	}

	public Clob getNotif_content() {
		return notif_content;
	}

	public void setNotif_content(Clob notif_content) {
		this.notif_content = notif_content;
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
				.append(", to_flat=").append(to_flat).append(", address=").append(address).append(", remote_notif_id=")
				.append(remote_notif_id).append(", notif_content=").append(notif_content).append(", sent_on=")
				.append(sent_on).append("]");
		return builder.toString();
	}

}
