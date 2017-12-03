package com.gc.component.listener;

public interface ProgressListener {

	void setProgressMax(int totalNotifications);

	void sent(String sentNotification);

	void intimate(String message);

	void markComplete();

}
