CREATE TABLE IF NOT EXISTS notification(id BIGINT AUTO_INCREMENT PRIMARY KEY, notif_type VARCHAR(20), to_flat VARCHAR(10), notif_content CLOB, notif_response CLOB, sent_on TIMESTAMP, status VARCHAR(20));
CREATE INDEX IF NOT EXISTS flat_no_index ON notification(to_flat);
CREATE INDEX IF NOT EXISTS sent_on_index ON notification(sent_on);