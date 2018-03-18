CREATE TABLE IF NOT EXISTS notification(id BIGINT AUTO_INCREMENT PRIMARY KEY, notif_type VARCHAR(20), to_flat VARCHAR(10), address VARCHAR(100), remote_notif_id VARCHAR(50), notif_content CLOB, sent_on TIMESTAMP);
CREATE INDEX IF NOT EXISTS flat_no_index ON notification(to_flat);
CREATE INDEX IF NOT EXISTS sent_on_index ON notification(sent_on);