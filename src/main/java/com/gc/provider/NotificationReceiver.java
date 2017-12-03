package com.gc.provider;

import java.util.List;

import com.gc.vo.Notification;

public interface NotificationReceiver {

	void receive(List<Notification> notifications);

}
