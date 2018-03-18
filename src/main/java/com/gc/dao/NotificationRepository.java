package com.gc.dao;

import org.springframework.data.repository.CrudRepository;

import com.gc.dao.entity.NotificationEntity;

public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {

}
