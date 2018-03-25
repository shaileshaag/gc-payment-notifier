package com.gc.dao;

import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.vo.partner.BusinessMantraResponse;

public class NotificationRepositoryDao {

	private static final String sql = "INSERT INTO notification (notif_type, to_flat, notif_content, notif_response, sent_on, status) VALUES "
			+ "(?, ?, ?, ?, ?, ?)";

	private final JdbcTemplate jdbcTemplate;

	private final ObjectMapper objectMapper;

	public NotificationRepositoryDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.objectMapper = new ObjectMapper();
	}

	@Transactional
	public void persistNotification(String flatNo, SimpleMailMessage message, String status)
			throws JsonProcessingException {
		String messageString = objectMapper.writeValueAsString(message);
		jdbcTemplate.update(sql,
				new Object[] { "EMAIL", flatNo, messageString, null, new java.sql.Timestamp(new Date().getTime()), status });
	}

	@Transactional
	public void persistNotification(String flatNo, Map<String, String> messageParams, String status,
			BusinessMantraResponse response) throws JsonProcessingException {
		String messageString = objectMapper.writeValueAsString(messageParams);
		String responseString = objectMapper.writeValueAsString(response);
		jdbcTemplate.update(sql, new Object[] { "SMS", flatNo, messageString, responseString,
				new java.sql.Date(new Date().getTime()), status });
	}

}
